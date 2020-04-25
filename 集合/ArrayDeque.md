### 1、概述

ArrayDeque 是 Deque 接口的变长数组实现，它没有容量限制，会根据需要来扩容。它不是线程安全的，缺乏外部的同步机制，导致它不支持多线程的并发访问。Null 对象是不许存在的，这个类很可能比 Stack 实现的栈和 LinkedList 实现的队列更快。

其绝大多数操作运行在常数时间，除了一些需要线性时间执行的方法：`remove`、`removeFirstOccurrence`、`removeLastOccurrence`、`contains`、`iterator.remove()`和批量操作。

它继承了 `AbstractCollection`，实现了`Deque`、`Cloneable` 和 `Serializable`。

```java
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable
```



### 2、变量

```java
// deque存放数据的数组，它的容量就是数组的长度，总是2的幂次方。数组不允许被充满，除非暂时性的用add方法导致数组充满，这里就要立刻扩容，去避免head和tail变得相同。同时，这里也保证数组中没有元素的位置始终为空。
transient Object[] elements;
transient int head;  // deque的头部，remove和pop的位置，deque为空时和tail相等
transient int tail;  // deque的尾部,addLast、add和push的位置
private static final int MIN_INITIAL_CAPACITY = 8;  // 新建deque的最小容量，必须为2的幂次方
```



### 3、构造方法

```java
public ArrayDeque() {
    elements = new Object[16];
}

public ArrayDeque(int numElements) {
    allocateElements(numElements);
}

private void allocateElements(int numElements) {
    int initialCapacity = MIN_INITIAL_CAPACITY;
    // Find the best power of two to hold elements.
    // Tests "<=" because arrays aren't kept full.
    if (numElements >= initialCapacity) {
        initialCapacity = numElements;
        initialCapacity |= (initialCapacity >>>  1);
        initialCapacity |= (initialCapacity >>>  2);
        initialCapacity |= (initialCapacity >>>  4);
        initialCapacity |= (initialCapacity >>>  8);
        initialCapacity |= (initialCapacity >>> 16);
        initialCapacity++;

        if (initialCapacity < 0)   // Too many elements, must back off
            initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
    }
    elements = new Object[initialCapacity];
}

public ArrayDeque(Collection<? extends E> c) {
    allocateElements(c.size());
    addAll(c);
}
```



### 4、重要方法

数组分配和大小调整的方法：

```java
private void doubleCapacity() {
    assert head == tail;
    int p = head;
    int n = elements.length;
    int r = n - p; // number of elements to the right of p
    int newCapacity = n << 1;
    if (newCapacity < 0)
        throw new IllegalStateException("Sorry, deque too big");
    Object[] a = new Object[newCapacity];
    System.arraycopy(elements, p, a, 0, r);
    System.arraycopy(elements, 0, a, r, p);
    elements = a;
    head = 0;
    tail = n;
}

private <T> T[] copyElements(T[] a) {
    if (head < tail) {
        System.arraycopy(elements, head, a, 0, size());
    } else if (head > tail) {
        int headPortionLen = elements.length - head;
        System.arraycopy(elements, head, a, 0, headPortionLen);
        System.arraycopy(elements, 0, a, headPortionLen, tail);
    }
    return a;
}
```

主要的插入和取出方法，包括 `addFirst` 、`addLast`、 `pollFirst` 、`pollLast`，其它的方法都是根据它们再定义的：

```java
public void addFirst(E e) {
    if (e == null)
        throw new NullPointerException();
    elements[head = (head - 1) & (elements.length - 1)] = e;
    if (head == tail)
        doubleCapacity();
}

public void addLast(E e) {
    if (e == null)
        throw new NullPointerException();
    elements[tail] = e;
    if ( (tail = (tail + 1) & (elements.length - 1)) == head)
        doubleCapacity();
}

public boolean offerFirst(E e) {
    addFirst(e);
    return true;
}

public boolean offerLast(E e) {
    addLast(e);
    return true;
}
```

