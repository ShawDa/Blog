## Nginx架构

nginx在unix系统上启动后，会以daemon的方式在后台运行，后台进程包含一个master进程和多个worker进程，可以看到nginx是以多进程的方式来工作的。

**master进程主要用来管理worker进程：**

> * 接收外界的信号并向worker进程发送
> * 监控worker进程的运行状态
> * 异常情况下当worker进程退出后自动重启新的worker进程

基本的网络事件就放在worker进程中处理。

多个worker进程之间相互独立且对等，同等竞争来自客户端的请求。

一个请求只能在一个worker进程中处理，一个worker进程不可能处理其它worker进程的请求。

worker进程个数是可以设置的，一般设为与计算机CPU的核数一致。

### Nginx进程模型：

![Nginx](imgs/nginx架构.png)

nginx启动后，master管理worker进程，所以外界只需给master进程发送信号就可以控制nginx了。

**reload nginx：**

nginx可以做到服务不中断的重启。当nginx收到重启命令时，首先master在收到信号后会先重新加载配置文件，然后再启动新的worker进程，并向所有老的worker发送信号告诉它们可以退出了。在新的worker启动后它们就开始接收新的请求，而老的worker在收到master的退出信号后就不在接收新的请求，并在当前进程所有请求处理完毕后就退出。

执行reload命令后会启动一个新的nginx进程，新的nginx进程在解析到reload参数后就知道命令的目的是控制nginx来重新加载配置文件了，它会向master进程发送信号，后续就如同上一段所说了。

**worker如何处理请求：**

worker进程之间是平等的，每个worker处理请求的机会一样。

HOW？每个worker都是从master进程fork过来的，在master里面，先建立好需要listen的socket（listenfd）之后再fork出多个worker。所有worker的listenfd会在新连接到来时变得可读，为保证只有一个进程处理该连接，所有worker在注册listenfd读事件前抢accept_mutex，抢到互斥锁的worker注册成功，在读事件里调用accept接受该连接。当一个worker在accept这个连接之后，就开始读取请求，解析请求，处理请求，产生数据后，再返回给客户端，最后才断开连接，这样就构成了一个完整的请求。可以看到，一个请求完全由worker来处理，而且只在一个worker中处理。

**进程模型的好处：**

> * 对于每个worker来说，独立的进程不需要加锁，省掉了所带来的开销
> * 独立进程之间相互不影响，某个进程退出后其它进程还在工作，服务不中断而且master会很快启动新的worker
> * worker异常退出则说明出现了error，会导致当前worker上所以的请求失败，但不会影响到其它worker上的请求，降低了风险

### Nginx事件处理模型：

nginx中的每个worker里只有一个主线程，所以采用了异步非阻塞的方式处理请求来实现高并发。因为若每个请求独占一个工作线程，当有几千个并发数时就同时有几千个线程在处理请求了，这些线程会带来非常大的内存占用，而且线程的上下文切换带来的CPU开销也很大，高并发也就无从说起了。

**一个请求的完整过程：**请求过来，建立连接，接收数据，处理数据，发送数据。这些操作具体到系统底层就是读写事件，而当读写事件没有准备好时，必然不能进行后续操作。若选择阻塞调用，那么当事件没有准备好时就只能等待，直到事件准备好再继续，阻塞调用会进入内核等待，CPU让出。这样对单个主线程的worker来说显然不好，当网络事件越多时大家都在等待，CPU空闲下来没人用，其利用率自然就上不去，高并发也就无从谈起了。若要加进程数，那就和线程模型没什么区别了，一样会增加无谓的上下文切换。

所以nginx里面不要阻塞的系统调用，而是用非阻塞的方式。非阻塞就是：当事件没有准备好时会马上返回EAGAIN，告诉请求事件还没准备好，过会再来。过一会后再来检查一下事件，直到事件准备好了为止，在这期间你就可以先去做其它事情。虽然不阻塞了，但你得不时地过来检查一下事件的状态，你可以做更多的事情了，但带来的开销也是不小的。所以才会有了异步非阻塞的事件处理机制，也叫做IO多路复用，具体到系统层面就是像select/poll/epoll/kqueue这样的系统调用。它们提供了一种机制：让你可以同时监控多个事件，调用他们是阻塞的，但可以设置超时时间，若有事件在超时时间内准备好了就返回。

这种机制正好解决了我们上面的两个问题，以epoll为例，当事件没准备好时，放到epoll里面，事件准备好了，我们就去读写，当读写返回EAGAIN时，我们将它再次加入到epoll里面。这样我们只在事件准备好了就去处理它，而当所有事件都没准备好时，才在epoll里面等着，如此一来就可以并发处理大量的并发了。当然，这里的并发请求指的是未处理完的请求，线程只有一个，所以同时能处理的请求当然只有一个了，只是在请求间进行不断地切换而已，切换也是因为异步事件未准备好而主动让出的。这里的切换是没有任何代价，你可以理解为循环处理多个准备好的事件，事实上就是这样的。与多线程相比，这种事件处理方式是有很大的优势的，不需要创建线程，每个请求占用的内存也很少，没有上下文切换，事件处理非常的轻量级。并发数再多也不会导致无谓的资源浪费（上下文切换），更多的并发数只是会占用更多的内存而已。

之前说过推荐设置worker的个数为cpu的核数，在这里就很容易理解了，更多的worker数只会导致进程来竞争cpu资源，从而带来不必要的上下文切换。而且，nginx为了更好的利用多核特性，提供了cpu亲缘性的绑定选项，我们可以将某一个进程绑定在某一个核上，这样就不会因为进程的切换带来cache的失效。

对于一个基本的web服务器来说，事件通常有三种类型：网络事件、信号、定时器。从上面的讲解中知道，网络事件通过异步非阻塞可以很好的解决掉，那如何处理信号与定时器呢？

对nginx来说，有一些特定的**信号**代表着特定的意义，信号会中断掉程序当前的运行，在改变状态后，继续执行。如果是系统调用，则可能会导致系统调用的失败，需要重入。对于nginx来说，如果nginx正在等待事件（epoll_wait时），若程序收到信号，在信号处理函数处理完后，epoll_wait会返回错误，然后程序可再次进入epoll_wait调用。

由于epoll_wait等函数在调用的时候是可以设置一个超时时间的，所以nginx借助这个超时时间来实现**定时器**。nginx里面的定时器事件是放在一颗维护定时器的红黑树里面，每次在进入epoll_wait前，先从该红黑树里面拿到所有定时器事件的最小时间，在计算出epoll_wait的超时时间后进入epoll_wait。所以，当没有事件产生，也没有中断信号时，epoll_wait会超时，也就是说，定时器事件到了。这时，nginx会检查所有的超时事件，将他们的状态设置为超时，然后再去处理网络事件。由此可以看出，当我们写nginx代码时，在处理网络事件的回调函数时，通常做的第一个事情就是判断超时，然后再去处理网络事件。



## Nginx基础概念

### connection

在nginx中connection就是对tcp连接的封装，其中包括连接的socket，读事件，写事件。利用nginx封装的connection，我们可以很方便的来处理与连接相关的事件，比如建立连接、发送与接受数据等。而且nginx中http请求的处理就是建立在connection之上的，所以nginx不仅可以作为一个web服务器，也可以作为邮件服务器。当然，利用nginx提供的connection，我们可以与任何后端服务打交道。

结合一个tcp连接的生命周期，我们看看nginx是如何处理一个连接的：

> * 首先，nginx在启动时会解析配置文件，得到需要监听的端口与ip地址，然后在nginx的master进程里面，先初始化好这个监控的socket(创建socket，设置addrreuse等选项，绑定到指定的ip地址端口，再listen)
> * 然后再fork出多个子进程出来，子进程会竞争accept新的连接，此时客户端就可以向nginx发起连接了
> * 当客户端与服务端通过三次握手建立好一个连接后，nginx的某一个子进程会accept成功，得到这个建立好的连接的socket，然后创建nginx对连接的封装，即ngx_connection_t结构体
> * 接着就设置读写事件处理函数并添加读写事件来与客户端进行数据的交换，最后nginx或客户端来主动关掉连接，至此一个连接就寿终正寝了。

当然，nginx也是可以作为客户端来请求其它server的（如upstream模块），此时与其它server创建的连接也封装在ngx_connection_t中。作为客户端，nginx先获取一个ngx_connection_t结构体，然后创建socket并设置socket的属性（ 比如非阻塞），再通过添加读写事件，调用connect/read/write来调用连接，最后关掉连接并释放ngx_connection_t。

在nginx中，每个进程会有一个连接数的最大上限，**这个上限与系统对fd的限制不一样**。在操作系统中，通过`ulimit -n` 我们可以得到一个进程所能够打开的fd的最大数，即nofile，因为每个socket连接会占用掉一个fd，所以这也会限制我们进程的最大连接数，当然也会直接影响到我们程序所能支持的最大并发数，当fd用完后，再创建socket时，就会失败。nginx通过设置worker_connectons来设置每个进程支持的最大连接数。如果该值大于nofile，那么实际的最大连接数是nofile，nginx会有警告。nginx在实现时，是通过一个连接池来管理的，每个worker进程都有一个独立的连接池，连接池的大小是worker_connections。这里的连接池里面保存的其实不是真实的连接，它只是一个worker_connections大小的一个ngx_connection_t结构的数组。并且，nginx会通过一个链表free_connections来保存所有的空闲ngx_connection_t，每次获取一个连接时，就从空闲连接链表中获取一个，用完后再放回空闲连接链表里面。

在这里，很多人会误解worker_connections这个参数的意思，认为这个值就是nginx所能建立连接的最大值。其实不然，这个值是**表示每个worker进程所能建立连接的最大值**，所以一个nginx能建立的最大连接数应该是worker_connections * worker_processes。当然，这里说的是最大连接数，对于HTTP请求本地资源来说，能够支持的最大并发数量是worker_connections * worker_processes，而如果是HTTP作为反向代理来说，最大并发数量应该是worker_connections * worker_processes/2。因为作为反向代理服务器，每个并发会建立与客户端和与后端服务的两个连接。

我们前面有说过一个客户端连接过来后，多个空闲的进程会竞争这个连接，很容易看到这种竞争会导致不公平，如果某个进程得到accept的机会比较多，它的空闲连接很快就用完了，如果不提前做一些控制，当accept到一个新的tcp连接后，因为无法得到空闲连接且无法将此连接转交给其它进程，最终会导致此tcp连接得不到处理，就中止掉了。很显然这是不公平的，有的进程有空余连接却没有处理机会，有的进程没有空余连接，却人为地丢弃连接。

**那么，如何解决这个问题呢？**

> * 首先，nginx的处理得先打开accept_mutex选项，只有获得了accept_mutex的进程才会去添加accept事件，也就是说nginx会控制进程是否添加accept事件,且nginx使用一个叫ngx_accept_disabled的变量来控制是否去竞争accept_mutex锁
> * 在第一段代码中计算ngx_accept_disabled的值，这个值是nginx单进程的所有连接总数的八分之一减去剩下的空闲连接数量得到的，当剩余连接数小于总连接数的八分之一时其值才大于0，而且剩余的连接数越小，这个值越大
> * 再看第二段代码，当ngx_accept_disabled大于0时不会去尝试获取accept_mutex锁，并且会将ngx_accept_disabled减1，于是每次执行到此处时都会去减1，直到小于0。不去获取accept_mutex锁就是等于让出获取连接的机会，很显然可以看出：当空余连接越少时，ngx_accept_disable越大，于是让出的机会就越多，这样其它进程获取锁的机会也就越大。不去accept，自己的连接就控制下来了，其它进程的连接池就会得到利用，这样nginx就控制了多进程间连接的平衡

```c++
ngx_accept_disabled = ngx_cycle->connection_n / 8 - ngx_cycle->free_connection_n;

if (ngx_accept_disabled > 0) {
    ngx_accept_disabled--;
} else {
    if (ngx_trylock_accept_mutex(cycle) == NGX_ERROR) {
        return;
    }
    if (ngx_accept_mutex_held) {
        flags |= NGX_POST_EVENTS;

    } else {
        if (timer == NGX_TIMER_INFINITE || timer > ngx_accept_mutex_delay)
        {
            timer = ngx_accept_mutex_delay;
        }
    }
}
```



### request

request在nginx中指的是http请求，具体到nginx中的数据结构是ngx_http_request_t，它是对一个http请求的封装。 我们知道：一个http请求包含请求行、请求头、请求体、响应行、响应头、响应体。

http请求是典型的请求-响应类型的网络协议，且http是文件协议，所以我们在分析请求行与请求头以及输出响应行与响应头时往往是一行一行的进行处理。

如果我们自己来写一个http服务器：

> * 通常在一个连接建立好后客户端会发送请求过来，然后我们读取一行数据，分析出请求行中包含的method、uri、http_version等信息
> * 然后再一行一行处理请求头，并根据请求method与请求头的信息来决定是否有请求体以及请求体的长度，之后再去读取请求体
> * 得到请求后，我们处理请求产生需要输出的数据，然后再生成响应行，响应头以及响应体，在将响应发送给客户端之后，一个完整的请求就处理完了。

当然这是最简单的webserver的处理方式，其实nginx也是这样做的，只是有一些小小的区别：比如当请求头读取完成后就开始进行请求的处理了，nginx通过ngx_http_request_t来保存解析请求与输出响应相关的数据。

那接下来，简要讲讲nginx是如何处理一个完整的请求的：

> * 对于nginx来说，一个请求是从ngx_http_init_request开始的，在这个函数中，会设置读事件为ngx_http_process_request_line，也就是说接下来的网络事件会由ngx_http_process_request_line来执行。从ngx_http_process_request_line的函数名可以看到，这就是来处理请求行的，正好与之前讲的处理请求的第一件事就是处理请求行一致
> * 通过ngx_http_read_request_header来读取请求数据。然后调用ngx_http_parse_request_line函数来解析请求行。nginx为提高效率，采用状态机来解析请求行，而且在进行method的比较时，没有直接使用字符串比较，而是将四个字符转换成一个整型，然后一次比较以减少cpu的指令数。很多人可能很清楚一个请求行包含请求的方法，uri，版本，却不知道其实在请求行中，也是可以包含有host的。比如一个请求GET http://www.taobao.com/uri HTTP/1.0这样一个请求行也是合法的，而且host是www.taobao.com，这个时候，nginx会忽略请求头中的host域，而以请求行中的这个为准来查找虚拟主机。另外，对于对于http0.9版来说是不支持请求头的，所以这里也是要特别的处理，在后面解析请求头时协议版本都是1.0或1.1。整个请求行解析到的参数，会保存到ngx_http_request_t结构当中
> * 在解析完请求行后，nginx会设置读事件的handler为ngx_http_process_request_headers，然后后续的请求就在ngx_http_process_request_headers中进行读取与解析
> * ngx_http_process_request_headers函数用来读取请求头，跟请求行一样，还是调用ngx_http_read_request_header来读取请求头，调用ngx_http_parse_header_line来解析一行请求头，解析到的请求头会保存到ngx_http_request_t的域headers_in中，headers_in是一个链表结构，保存所有的请求头。而HTTP中有些请求是需要特别处理的，这些请求头与请求处理函数存放在一个映射表里面，即ngx_http_headers_in，在初始化时，会生成一个hash表，当每解析到一个请求头后，就会先在这个hash表中查找，如果有找到则调用相应的处理函数来处理这个请求头。比如:Host头的处理函数是ngx_http_process_host
> * 当nginx解析到两个回车换行符时，就表示请求头的结束，此时就会调用ngx_http_process_request来处理请求了
> * ngx_http_process_request会设置当前的连接的读写事件处理函数为ngx_http_request_handler，然后再调用ngx_http_handler来真正开始处理一个完整的http请求。这里可能比较奇怪：读写事件处理函数都是ngx_http_request_handler，其实在这个函数中会根据当前事件是读事件还是写事件分别调用ngx_http_request_t中的read_event_handler或者是write_event_handler
> * 由于此时我们的请求头已经读取完成了，且nginx的做法是先不读取请求body，所以这里面我们设置read_event_handler为ngx_http_block_reading，即不读取数据了。刚才说到，真正开始处理数据，是在ngx_http_handler这个函数里面，这个函数会设置write_event_handler为ngx_http_core_run_phases，并执行ngx_http_core_run_phases函数。ngx_http_core_run_phases这个函数将执行多阶段请求处理，nginx将一个http请求的处理分为多个阶段，那么这个函数就是执行这些阶段来产生数据。因为ngx_http_core_run_phases最后会产生数据，所以我们就很容易理解，为什么设置写事件的处理函数为ngx_http_core_run_phases了。在这里，我简要说明了一下函数的调用逻辑，我们需要明白最终是调用ngx_http_core_run_phases来处理请求，产生的响应头会放在ngx_http_request_t的headers_out中
> * nginx的各种阶段会对请求进行处理，最后会调用filter来过滤数据，对数据进行加工，如truncked传输、gzip压缩等。这里的filter包括header filter与body filter，即对响应头或响应体进行处理。filter是一个链表结构，分别有header filter与body filter，先执行header filter中的所有filter，然后再执行body filter中的所有filter。在header filter中的最后一个filter，即ngx_http_header_filter，这个filter将会遍历所有的响应头，最后需要输出的响应头在一个连续的内存，然后调用ngx_http_write_filter进行输出。ngx_http_write_filter是body filter中的最后一个，所以nginx首先的body信息，在经过一系列的body filter之后，最后也会调用ngx_http_write_filter来进行输出。

这里要注意的是，nginx会将整个请求头都放在一个buffer里面，这个buffer的大小通过配置项client_header_buffer_size来设置，如果用户的请求头太大，这个buffer装不下，那nginx就会重新分配一个新的更大的buffer来装请求头，这个大buffer可以通过large_client_header_buffers来设置，比如配置4 8k就是表示有四个8k大小的buffer可以用。注意，为了保存请求行或请求头的完整性，一个完整的请求行或请求头，需要放在一个连续的内存里面，所以一个完整的请求行或请求头只会保存在一个buffer里面。这样如果请求行大于一个buffer的大小，就会返回414错误，如果一个请求头大小大于一个buffer大小就会返回400错误。在了解了这些参数的值以及nginx实际的做法之后，在实际应用场景我们就需要根据实际的需求调整这些参数来优化我们的程序了。

nginx处理请求流程图：

![](imgs/nginx_request.png)

### keepalive

什么是长连接呢？我们知道http请求是基于TCP协议之上的，那么当客户端在发起请求前，需要先与服务端建立TCP连接，而每一次的TCP连接是需要三次握手来确定的，如果客户端与服务端之间网络差一点，那么这三次交互消费的时间会比较多，而且三次交互也会带来网络流量；当连接断开后也会有四次的交互，当然这对用户体验来说就不重要了。而http请求是请求应答式的，如果我们能知道每个请求头与响应体的长度，那么我们是可以在一个连接上面执行多个请求的，这就是所谓的长连接，但前提条件是我们先得确定请求头与响应体的长度。对于请求来说，如果当前请求需要有body，如POST请求，那么nginx就需要客户端在请求头中指定content-length来表明body的大小，否则返回400错误。也就是说，请求体的长度是确定的，那么响应体的长度呢？先来看看http协议中关于响应body长度的确定：

1. 对于http1.0协议来说，如果响应头中有content-length头，则以content-length的长度就可以知道body的长度了，客户端在接收body时，就可以依照这个长度来接收数据，接收完后就表示这个请求完成了。而如果没有content-length头，则客户端会一直接收数据，直到服务端主动断开连接，才表示body接收完了。
2. 而对于http1.1协议来说，如果响应头中的Transfer-encoding为**chunked传输**，则表示body是流式输出，body会被分成多个块，每块的开始会标识出当前块的长度，此时body不需要通过长度来指定。**如果是非chunked传输，而且有content-length**，则按照content-length来接收数据。**否则如果是非chunked，并且没有content-length，**则客户端接收数据，直到服务端主动断开连接。

从上面我们可以看到，除了http1.0不带content-length以及http1.1非chunked不带content-length外，body的长度是可知的，此时当服务端在输出完body之后，会可以考虑使用长连接。

能否使用长连接也是有条件限制的：

> * 如果客户端的请求头中的connection为close，则表示客户端需要关掉长连接，如果为keep-alive，则客户端需要打开长连接，如果客户端的请求中没有connection这个头，那么根据协议，如果是http1.0，则默认为close，如果是http1.1，则默认为keep-alive
> * 如果结果为keepalive，那么nginx在输出完响应体后，会设置当前连接的keepalive属性然后等待客户端下一次请求。当然，nginx不可能一直等待下去，如果客户端一直不发数据过来，岂不是一直占用这个连接？所以当nginx设置了keepalive等待下一次的请求时，同时也会设置一个最大等待时间，这个时间是通过选项keepalive_timeout来配置的，如果配置为0，则表示关掉keepalive，此时，http版本无论是1.1还是1.0，客户端的connection不管是close还是keepalive，都会强制为close
> * 如果服务端最后的决定是keepalive打开，那么在响应的http头里面，也会包含有connection头域，其值是”Keep-Alive”，否则就是”Close”。如果connection值为close，那么在nginx响应完数据后，会主动关掉连接。所以对于请求量比较大的nginx来说，关掉keepalive最后会产生比较多time-wait状态的socket。一般来说，当客户端的一次访问需要多次访问同一个server时，打开keepalive的优势非常大，比如图片服务器，通常一个网页会包含很多个图片,打开keepalive也会大量减少time-wait的数量。

### pipe

在http1.1中，引入了一种新的特性，即pipeline。那么什么是pipeline呢？pipeline其实就是流水线作业，它可以看作为keepalive的一种升华，因为pipeline也是基于长连接的，目的就是利用一个连接做多次请求。

如果客户端要提交多个请求，对于keepalive来说，那么第二个请求必须要等到第一个请求的响应接收完全后才能发起，这和TCP的停止等待协议是一样的，得到两个响应的时间至少为2*RTT。而对pipeline来说，客户端不必等到第一个请求处理完后，就可以马上发起第二个请求。得到两个响应的时间可能能够达到RTT。nginx是直接支持pipeline的，但是nginx对pipeline中的多个请求的处理却不是并行的，依然是一个请求接一个请求的处理，只是在处理第一个请求的时候，客户端就可以发起第二个请求，这样nginx利用pipeline减少了处理完一个请求后等待第二个请求的请求头数据的时间。

其实nginx的做法很简单，前面说到，nginx在读取数据时会将读取的数据放到一个buffer里面，所以如果nginx在处理完前一个请求后，如果发现buffer里面还有数据就认为剩下的数据是下一个请求的开始，然后就接下来处理下一个请求，否则就设置keepalive。

### lingering_close

lingering_close，字面意思就是延迟关闭，也就是说当nginx要关闭连接时，并非立即关闭连接，而是先关闭tcp连接的写，再等待一段时间后再关掉连接的读。

为什么要这样呢？我们先来看看这样一个场景：nginx在接收客户端的请求时，可能由于客户端或服务端出错了，要立即响应错误信息给客户端，而nginx在响应错误信息后，大分部情况下是需要关闭当前连接。nginx执行完write()系统调用把错误信息发送给客户端，write()系统调用返回成功并不表示数据已经发送到客户端，有可能还在tcp连接的write buffer里。接着如果直接执行close()系统调用关闭tcp连接，内核会首先检查tcp的read buffer里有没有客户端发送过来的数据留在内核态没有被用户态进程读取，如果有则发送给客户端RST报文来关闭tcp连接丢弃write buffer里的数据，如果没有则等待write buffer里的数据发送完毕，然后再经过正常的4次分手报文断开连接。所以,当在某些场景下出现tcp write buffer里的数据在write()系统调用之后到close()系统调用执行之前没有发送完毕，且tcp read buffer里面还有数据没有读，close()系统调用会导致客户端收到RST报文且不会拿到服务端发送过来的错误信息数据。那客户端肯定会想，这服务器好霸道，动不动就reset我的连接，连个错误信息都没有。

在上面这个场景中我们可以看到，关键点是服务端给客户端发送了RST包导致自己发送的数据在客户端忽略掉了。所以，解决问题的重点是让服务端别发RST包。再想想，我们发送RST是因为我们关掉了连接，关掉连接是因为我们不想再处理此连接了，也不会有任何数据产生了。对于全双工的TCP连接来说，我们只需要关掉写就行了，读可以继续进行，我们只需要丢掉读到的任何数据就行了，这样的话，当我们关掉连接后，客户端再发过来的数据，就不会再收到RST了。当然最终我们还是需要关掉这个读端的，所以我们会设置一个超时时间，在这个时间过后，就关掉读，客户端再发送数据来就不管了，作为服务端我会认为，都这么长时间了，发给你的错误信息也应该读到了，再慢就不关我事了。当然，正常的客户端，在读取到数据后，会关掉连接，此时服务端就会在超时时间内关掉读端。

这些正是lingering_close所做的事情。协议栈提供 SO_LINGER 这个选项，它的一种配置情况就是来处理lingering_close的情况的，不过nginx是自己实现的lingering_close。lingering_close存在的意义就是来读取剩下的客户端发来的数据，所以nginx会有一个读超时时间，通过lingering_timeout选项来设置，如果在lingering_timeout时间内还没有收到数据，则直接关掉连接。nginx还支持设置一个总的读取时间，通过lingering_time来设置，这个时间也就是nginx在关闭写之后，保留socket的时间，客户端需要在这个时间内发送完所有的数据，否则nginx在这个时间过后，会直接关掉连接。当然，nginx是支持配置是否打开lingering_close选项的，通过lingering_close选项来配置。

那么，我们在实际应用中是否应该打开lingering_close呢？这个就没有固定的推荐值了，如Maxim Dounin所说，lingering_close的主要作用是保持更好的客户端兼容性，但是却需要消耗更多的额外资源（比如连接会一直占着）。



## Nginx配置系统

nginx的配置系统由一个主配置文件和其他一些辅助的配置文件构成，这些配置文件均是纯文本文件，全部位于nginx安装目录下的conf目录下。

配置文件中以#开始的行，或者是前面有若干空格或者TAB然后再跟#的行，都被认为是注释，也就是只对编辑查看文件的用户有意义，程序在读取这些注释行的时候其实际的内容是被忽略的。

由于除主配置文件nginx.conf以外的文件都是在某些情况下才使用的，而只有主配置文件是在任何情况下都被使用的，所以在这里我们就以主配置文件为例来解释nginx的配置系统。

nginx.conf中包含若干配置项，每个配置项由配置指令和指令参数2个部分构成，指令参数也就是配置指令对应的配置值。

### 指令概述

配置指令是一个字符串，可以用单引号或者双引号括起来，也可以不括。**但是如果配置指令包含空格，一定要引起来。**

### **指令参数**

指令的参数使用一个或者多个空格或者TAB字符与指令分开，指令的参数由一个或者多个TOKEN串组成，TOKEN串之间由空格或者TAB键分隔。

TOKEN串分为简单字符串或者是复合配置块，复合配置块即是由大括号括起来的一堆内容，一个复合配置块中可能包含若干其他的配置指令。

如果一个配置指令的参数全部由简单字符串构成，也就是不包含复合配置块，那么我们就说这个配置指令是一个简单配置项，否则称之为复杂配置项。例如下面这个是一个简单配置项：

```shell
error_page   500 502 503 504  /50x.html;
```

对于简单配置，配置项的结尾使用分号结束。对于复杂配置项，包含多个TOKEN串的，一般都是简单TOKEN串放在前面，复合配置块一般位于最后，而且其结尾并不需要再添加分号。例如下面这个复杂配置项：

```shell
location / {
    root   /home/jizhao/nginx-book/build/html;
    index  index.html index.htm;
}
```

### 指令上下文

nginx.conf中的配置信息，根据其逻辑上的意义对它们进行了分类，也就是分成了多个作用域，或者称之为配置指令上下文，不同的作用域含有一个或者多个配置项。

当前nginx支持的几个指令上下文：

| 名称     | 描述                                                         |
| :------- | ------------------------------------------------------------ |
| main     | nginx在运行时与具体业务功能（比如http服务或者email服务代理）无关的一些参数，比如工作进程数，运行的身份等。 |
| http     | 与提供http服务相关的一些配置参数，例如：是否使用keepalive啊、是否使用gzip进行压缩等。 |
| server   | http服务上支持若干虚拟主机，每个虚拟主机一个对应的server配置项，配置项里面包含该虚拟主机相关的配置。在提供mail服务的代理时，也可以建立若干server。每个server通过监听的地址来区分。 |
| location | http服务中，某些特定的URL对应的一系列配置项。                |
| mail     | 实现email相关的SMTP/IMAP/POP3代理时共享的一些配置项（因为可能实现多个代理，工作在多个监听地址上）。 |

指令上下文可能有包含的情况出现，例如：通常http上下文和mail上下文一定是出现在main上下文里的。在一个上下文里可能包含另外一种类型的上下文多次，例如：如果http服务支持了多个虚拟主机，那么在http上下文里就会出现多个server上下文。

我们来看一个示例配置：

```shell
user  nobody;
worker_processes  1;
error_log  logs/error.log  info;

events {
    worker_connections  1024;
}

http {
    server {
        listen          80;
        server_name     www.linuxidc.com;
        access_log      logs/linuxidc.access.log main;
        location / {
            index index.html;
            root  /var/www/linuxidc.com/htdocs;
        }
    }

    server {
        listen          80;
        server_name     www.Androidj.com;
        access_log      logs/androidj.access.log main;
        location / {
            index index.html;
            root  /var/www/androidj.com/htdocs;
        }
    }
}

mail {
    auth_http  127.0.0.1:80/auth.php;
    pop3_capabilities  "TOP"  "USER";
    imap_capabilities  "IMAP4rev1"  "UIDPLUS";

    server {
        listen     110;
        protocol   pop3;
        proxy      on;
    }
    server {
        listen      25;
        protocol    smtp;
        proxy       on;
        smtp_auth   login plain;
        xclient     off;
    }
}
```

在这个配置中，上面提到个五种配置指令上下文都存在。

存在于main上下文中的配置指令如下:

- user
- worker_processes
- error_log
- events
- http
- mail

存在于http上下文中的指令如下:

- server

存在于mail上下文中的指令如下：

- server
- auth_http
- imap_capabilities

存在于server上下文中的配置指令如下：

- listen
- server_name
- access_log
- location
- protocol
- proxy
- smtp_auth
- xclient

存在于location上下文中的指令如下：

- index
- root



## Nginx模块化体系结构

nginx的内部结构是由核心部分和一系列的功能模块所组成，这样划分是为了使得每个模块的功能相对简单，便于开发，同时也便于对系统进行功能扩展。为了便于描述，下文中我们将使用nginx core来称呼nginx的核心功能部分。

nginx提供了web服务器的基础功能，同时提供了web服务反向代理，email服务反向代理功能。nginx core实现了底层的通讯协议，为其他模块和nginx进程构建了基本的运行时环境，并且构建了其他各模块的协作基础。除此之外，大部分与协议相关的或者应用相关的功能都是在这些模块中所实现的。

### 模块概述

nginx将各功能模块组织成一条链，当有请求到达的时候，请求依次经过这条链上的部分或者全部模块进行处理，每个模块实现特定的功能。例如：实现对请求解压缩的模块，实现SSI的模块，实现与上游服务器进行通讯的模块，实现与FastCGI服务进行通讯的模块。

有两个模块比较特殊，他们居于nginx core和各功能模块的中间，这两个模块就是http模块和mail模块。这2个模块在nginx core之上实现了另外一层抽象，处理与HTTP协议和email相关协议（SMTP/POP3/IMAP）有关的事件，并且确保这些事件能被以正确的顺序调用其他的一些功能模块。

目前HTTP协议是被实现在http模块中的，但是有可能将来被剥离到一个单独的模块中，以扩展nginx支持SPDY协议。

### 模块分类

nginx的模块根据其功能基本上可以分为以下几种类型：

| 模块名称      | 描述                                                         |
| :------------ | ------------------------------------------------------------ |
| event module  | 搭建了独立于操作系统的事件处理机制的框架，提供了各具体事件的处理，包括ngx_events_module， ngx_event_core_module和ngx_epoll_module等。nginx具体使用何种事件处理模块，这依赖于具体的操作系统和编译选项。 |
| phase handler | 此类型的模块也被直接称为handler模块，主要负责处理客户端请求并产生待响应内容，比如ngx_http_static_module模块负责客户端的静态页面请求处理并将对应的磁盘文件准备为响应内容输出。 |
| output filter | 也称为filter模块，主要是负责对输出的内容进行处理，可以对输出进行修改。例如，可以实现对输出的所有html页面增加预定义的footbar一类的工作，或者对输出图片的URL进行替换之类的工作。 |
| upstream      | upstream模块实现反向代理的功能，将真正的请求转发到后端服务器上，并从后端服务器上读取响应发回客户端。upstream模块是一种特殊的handler，只不过响应内容不是真正由自己产生的，而是从后端服务器上读取的。 |
| load-balancer | 负载均衡模块，实现特定的算法，在众多的后端服务器中选择一个服务器出来作为某个请求的转发服务器。 |



## Nginx请求处理

nginx使用一个多进程模型来对外提供服务，其中一个master进程，多个worker进程。master进程负责管理nginx本身和其他worker进程。

所有实际上的业务处理逻辑都在worker进程，worker进程中有一个函数执行无限循环，不断处理收到的来自客户端的请求并进行处理，直到整个nginx服务被停止。

worker进程中，ngx_worker_process_cycle()函数就是这个无限循环的处理函数。在这个函数中，一个请求的简单处理流程如下：

1. 操作系统提供的机制（例如epoll, kqueue等）产生相关的事件。
2. 接收和处理这些事件，如是接受到数据则产生更高层的request对象。
3. 处理request的header和body。
4. 产生响应，并发送回客户端。
5. 完成request的处理。
6. 重新初始化定时器及其他事件。

### 请求处理流程

为了让大家更好的了解nginx中请求处理过程，我们以HTTP Request为例，来做一下详细地说明。

从nginx的内部来看，一个HTTP Request的处理过程涉及到以下几个阶段：

1. 初始化HTTP Request（读取来自客户端的数据并生成HTTP Request对象，该对象含有该请求所有的信息）。
2. 处理请求头。
3. 处理请求体。
4. 如果有的话，调用与此请求（URL或者Location）关联的handler。
5. 依次调用各phase handler进行处理。

phase字面的意思就是阶段，所以phase handlers也就好理解了，就是包含若干个处理阶段的一些handler。

在每一个阶段，包含有若干个handler，再处理到某个阶段的时候，依次调用该阶段的handler对HTTP Request进行处理。

通常情况下，一个phase handler对这个request进行处理并产生一些输出，通常phase handler是与定义在配置文件中的某个location相关联的。

一个phase handler通常执行以下几项任务：

1. 获取location配置。
2. 产生适当的响应。
3. 发送response header。
4. 发送response body。

当nginx读取到一个HTTP Request的header的时候，nginx首先查找与这个请求关联的虚拟主机的配置。如果找到了这个虚拟主机的配置，那么通常情况下，这个HTTP Request将会经过以下几个阶段的处理（phase handlers）：

| 名称                          | 内容                     |
| :---------------------------- | ------------------------ |
| NGX_HTTP_POST_READ_PHASE      | 读取请求内容阶段         |
| NGX_HTTP_SERVER_REWRITE_PHASE | Server请求地址重写阶段   |
| NGX_HTTP_FIND_CONFIG_PHASE    | 配置查找阶段             |
| NGX_HTTP_REWRITE_PHASE        | Location请求地址重写阶段 |
| NGX_HTTP_POST_REWRITE_PHASE   | 请求地址重写提交阶段     |
| NGX_HTTP_PREACCESS_PHASE      | 访问权限检查准备阶段     |
| NGX_HTTP_ACCESS_PHASE         | 访问权限检查阶段         |
| NGX_HTTP_POST_ACCESS_PHASE    | 访问权限检查提交阶段     |
| NGX_HTTP_TRY_FILES_PHASE      | 配置项try_files处理阶段  |
| NGX_HTTP_CONTENT_PHASE        | 内容产生阶段             |
| NGX_HTTP_LOG_PHASE            | 日志模块处理阶段         |

在内容产生阶段，为了给一个request产生正确的响应，nginx必须把这个request交给一个合适的content handler去处理。如果这个request对应的location在配置文件中被明确指定了一个content handler，那么nginx就可以通过对location的匹配，直接找到这个对应的handler，并把这个request交给这个content handler去处理。这样的配置指令包括像perl，flv，proxy_pass，mp4等。

如果一个request对应的location并没有直接有配置的content handler，那么nginx依次尝试:

1. 如果一个location里面有配置 random_index on，那么随机选择一个文件发送给客户端。
2. 如果一个location里面有配置 index指令，那么发送index指令指明的文件给客户端。
3. 如果一个location里面有配置 autoindex on，那么就发送请求地址对应服务端路径下的文件列表给客户端。
4. 如果这个request对应的location上有设置gzip_static on，那么就查找是否有对应的.gz文件存在，有的话就发送这个给客户端（客户端支持gzip的情况下）。
5. 请求的URI如果对应一个静态文件，static module就发送静态文件的内容到客户端。

内容产生阶段完成以后，生成的输出会被传递到filter模块去进行处理，filter模块也是与location相关的，所有的fiter模块都被组织成一条链，输出会依次穿越所有的filter，直到有一个filter模块的返回值表明已经处理完成。

这里列举几个常见的filter模块，例如：

1. server-side includes。
2. XSLT filtering。
3. 图像缩放之类的。
4. gzip压缩。

在所有的filter中，有几个filter模块需要关注一下，按照调用的顺序依次说明如下：

| 名称     | 描述                                                         |
| :------- | ------------------------------------------------------------ |
| write    | 写输出到客户端，实际上是写到连接对应的socket上。             |
| postpone | 这个filter是负责subrequest也就是子请求的。                   |
| copy     | 将一些需要复制的buf(文件或者内存)重新复制一份然后交给剩余的body filter处理。 |



## handler模块

相信大家在看了前一章的模块概述以后，都对nginx的模块有了一个基本的认识。基本上作为第三方开发者最可能开发的就是三种类型的模块，即handler，filter和load-balancer。Handler模块就是接受来自客户端的请求并产生输出的模块，有些地方说upstream模块实际上也是一种handler模块，只不过它产生的内容来自于从后端服务器获取的而非在本机产生的。

上面提到过配置文件中使用location指令可以配置content handler模块，当Nginx系统启动的时候，每个handler模块都有一次机会把自己关联到对应的location上。如果有多个handler模块都关联了同一个location，那么实际上只有一个handler模块真正会起作用，当然大多数情况下模块开发人员都会避免出现这种情况。

handler模块处理的结果通常有三种情况: 处理成功，处理失败（处理的时候发生了错误）或者是拒绝去处理。在拒绝处理的情况下，这个location的处理就会由默认的handler模块来进行处理。例如当请求一个静态文件的时候，如果关联到这个location上的一个handler模块拒绝处理，就会由默认的ngx_http_static_module模块进行处理，该模块是一个典型的handler模块。



## filter模块

过滤（filter）模块是过滤响应头和内容的模块，可以对回复的头和内容进行处理。它的处理时间在获取回复内容之后并在向用户发送响应之前，它的处理过程分为两个阶段：过滤HTTP回复的头部和主体，在这两个阶段可以分别对头部和主体进行修改。



## upstream模块

nginx模块一般被分成三大类：handler、filter和upstream，之前已经了解了handler、filter，利用这两类模块可以使nginx轻松完成任何单机工作，而这里介绍的upstream模块将使nginx跨越单机的限制，完成网络数据的接收、处理和转发。

数据转发功能为nginx提供了跨越单机的横向处理能力，使nginx摆脱只能为终端节点提供单一功能的限制，而使它具备了网路应用级别的拆分、封装和整合的战略功能。在云模型大行其道的今天，数据转发是nginx有能力构建一个网络应用的关键组件。当然，鉴于开发成本的问题，一个网络应用的关键组件一开始往往会采用高级编程语言开发，但是当系统到达一定规模，并且需要更重视性能的时候，为了达到所要求的性能目标，高级语言开发出的组件必须进行结构化修改。此时，对于修改代价而言，nginx的upstream模块呈现出极大的吸引力，因为它天生就快，而且nginx配置系统提供的层次化和松耦合使得系统的扩展性也达到比较高的程度。



## Reference

[Nginx开发从入门到精通](http://tengine.taobao.org/book/)

