### 1、概述

Java 的 volatile 关键字被用来修饰“被存储在主存中”的变量，更确切的说，每次读取 volatile 变量都是从主存中读而不是 CPU 缓存中，每次写 volatile 变量都会写到主存中而不仅仅是缓存中。（注意这里说的是在对变量修改的情况下。可见性是通过总线的通知机制实现：当某个线程修改了变量值，会标记缓存失效，CPU 到主存读取变量；未修改的情况下，若缓存中存在变量还是直接从缓存中直接读取）

### 2、变量可见性问题

Java 的 volatile 关键字保证了对各线程变量改变的可见性。

在一个多线程应用中，处于性能考虑，线程在操作非 volatile 变量时每个线程可能会将变量从主存拷贝到缓存中。若计算机有多个 CPU，那么每个线程可能会在不同的 CPU 中运行，这意味着每个线程都可能会把变量拷贝到各自缓存中：

![](imgs/java-volatile.png)

对于非 volatile 变量，JVM 并不保证会从主存中读取数据到缓存，或将缓存中的数据写入到主存中。

假设两个线程访问一个共享变量 count，若线程1修改了该变量值，但线程1和2都可能在某个时刻读取该值。如果 count 变量没有声明 volatile，那么修改的值不保证会从缓存中写回到主存，也就是缓存和主存中的变量值不一致。

这就是“可见性”问题，指的是线程看不到最新的变量值，因为其它线程还未将值写入主存，也就是一个线程的修改对其它的线程是不可见的。

### 3、volatile 可见性保证

将变量声明为 volatile，那么在写入时也会同时将变量值写入到主存中，在读取时会直接从主存中读取，保证变量的写入对其它线程可见。

上面的例子中，线程1修改了变量，线程2去读取该变量，将该变量声明为 volatile 就能保证线程1的写入对线程2是可见的。

然而，若两个线程都修改了变量的值，只声明 volatile 是不够的。

### 4、完整的 volatile 可见性保证

volatile 的可见性保证并不只对修饰的变量本身，还遵循以下原则：

> * 线程1写入某个 volatile 变量，线程2随后读取了该变量，那么线程1在写入 volatile 变量之前的所有可见的变量值在线程2读取 volatile 变量后都是可见的
> * 若线程1读取一个 volatile 变量，那么该线程中所有可见的变量同样从主存中重新读取

```java
public class Test {
    private int years;
    private int months;
    private volatile int days;

    public void update(int years, int months, int days){
        this.years  = years;
        this.months = months;
        this.days   = days;
    }
    
    public int totalDays() {
        int total = this.days;
        total += this.months * 30;
        total += this.years * 365;
        return total;
    }
}
```

`update()` 方法写入了3个变量，其中只有 days 变量是 volatile 的。完整的可见性保证意味着：在写入 days 变量时，其它两个变量也会被写入到主存中。`totalDays()` 中最开始读取 days 变量值，同时也会在主存中读取其它两个变量的值 。

可以将对 volatile 变量的读写理解为一个触发刷新的操作，写入时线程中的所有变量都会触发写入主存，而读取时也同样会触发这些变量从主存中读取。因此，应该尽量将 volatile 变量的写操作放在后面，读操作放在前面，这样就能连带其它变量一起刷新。若 `update()` 方法中先写入 days 变量，那么它会写入主存而其它两个就不会。若 `totalDays()` 方法中最后加上 days 变量，那么另外两个变量可能还是从缓存中而不是主存中读取的。

### 5、指令重排问题和 Happens-Before 保证

### 6、何时使用

### 7、性能考量

