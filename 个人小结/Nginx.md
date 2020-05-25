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

#### Nginx进程模型：

![Nginx](imgs/nginx架构.png)

nginx启动后，master管理worker进程，所以外界只需给master进程发送信号就好了。

**reload nginx：**





## Reference

[Nginx开发从入门到精通](http://tengine.taobao.org/book/)

