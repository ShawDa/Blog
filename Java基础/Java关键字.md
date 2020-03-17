### 1.访问控制
| 访问权限 | 当前类 | 包 | 子类 | 其它包 |
|----|----|----|----|---- |
public | ∨	| ∨ | ∨ | ∨ |
protect | ∨ | ∨ | ∨ | × |
default	| ∨ | ∨ | × | × |
private	| ∨ | × | × | × |

### 2.final
final修饰基本类型的变量不能再被赋值，其值无法改变；final修饰引用类型变量时，可以保证变量不能被再次赋值，但对象值可能改变。

```java
final StringBuilder sb = new StringBuilder("Hello");
sb = new StringBuilder("hello");  // Cannot assign
sb.append("World");  // OK
```

final修饰方法，该方法不能在子类里被重写。

final修饰类，该类无法被继承，String类就是一个final类。

### 3.static
static变量也被称为静态变量，没有用static修饰的变量被称为实例变量，它们的区别：
静态变量随着类加载时完成初始化，它在内存中仅有一个，且JVM只会为它分配一次内存，同时类的实例都共享静态变量，可以直接通过类名来访问；
实例变量则伴随着实例，每创建一个实例就会产生一个实例变量，它们同生共死。

static方法又称为静态方法，可以不依赖任何对象而被访问，没有对象，也就没有this、super。
静态方法中不能访问类的非静态成员变量和非静态成员方法，因为它们必须依赖具体的对象才能被调用，但非静态成员方法可以访问静态成员方法/变量。

被static修饰的代码块又称静态代码块，它会随着类的加载一块执行初始化，主要用以优化程序性能。

static修饰的类只能是内部类，被static所修饰的内部类可以用new关键字来直接创建一个实例，不需要先创建外部类实例。

### 4.synchronized
https://www.cnblogs.com/blueSkyline/p/8598099.html

https://blog.csdn.net/qq_38011415/article/details/89047812

https://blog.csdn.net/zhangqiluGrubby/article/details/80500505

修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象。

修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象。

修饰一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象。

修饰一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。

### 5.[volatile](../并发/volatile关键字.md)

https://www.cnblogs.com/dolphin0520/p/3920373.html

https://www.cnblogs.com/paddix/p/5428507.html

防止重排序，实现可见性，保持原子性。

**后两个关键字会在并发里面详细说明**