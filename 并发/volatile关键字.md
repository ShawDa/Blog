### 1、概述

Java 的 volatile 关键字被用来修饰“被存储在主存中”的变量，更确切的说，每次读取 volatile 变量都是从主存中读而不是 CPU 缓存中，每次写 volatile 变量都会写到主存中而不仅仅是缓存中。

### 2、变量可见性问题

Java 的 volatile 关键字保证了对各线程变量改变的可见性。



![](imgs/java-volatile.png)

### 3、volatile 可见性保证

### 4、完整的 volatile 可见性保证

### 5、指令重排问题和 Happens-Before 保证

### 6、何时使用

### 7、性能考量