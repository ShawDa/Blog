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
    elements = new Object[16];  // 默认长度16
}

public ArrayDeque(int numElements) {
    allocateElements(numElements);
}

// 类似HashMap的方法分配数组长度，2的幂次方且刚比指定的长度大
private void allocateElements(int numElements) {
    int initialCapacity = MIN_INITIAL_CAPACITY;
    // Find the best power of two to hold elements.
    // Tests "<=" because arrays aren't kept full.
    if (numElements >= initialCapacity) {
        initialCapacity = numElements;
        initialCapacity |= (initialCapacity >>>  1);  // 按位或，有一个为1就为1
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
    addAll(c);  // AbstractCollection中的方法
}
```



### 4、重要方法

数组分配和大小调整的方法：

```java
private void doubleCapacity() {  // 当新增数据导致头尾重合时就要double数组容量
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

public E removeFirst() {
    E x = pollFirst();
    if (x == null)
        throw new NoSuchElementException();
    return x;
}

public E removeLast() {
    E x = pollLast();
    if (x == null)
        throw new NoSuchElementException();
    return x;
}

public E pollFirst() {
    int h = head;
    @SuppressWarnings("unchecked")
    E result = (E) elements[h];
    // Element is null if deque empty
    if (result == null)
        return null;
    elements[h] = null;     // Must null out slot
    head = (h + 1) & (elements.length - 1);
    return result;
}

public E pollLast() {
    int t = (tail - 1) & (elements.length - 1);
    @SuppressWarnings("unchecked")
    E result = (E) elements[t];
    if (result == null)
        return null;
    elements[t] = null;
    tail = t;
    return result;
}

public E getFirst() {
    @SuppressWarnings("unchecked")
    E result = (E) elements[head];
    if (result == null)
        throw new NoSuchElementException();
    return result;
}

public E getLast() {
    @SuppressWarnings("unchecked")
    E result = (E) elements[(tail - 1) & (elements.length - 1)];
    if (result == null)
        throw new NoSuchElementException();
    return result;
}

public E peekFirst() {
    // elements[head] is null if deque empty
    return (E) elements[head];
}

public E peekLast() {
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }

public boolean removeFirstOccurrence(Object o) {
    if (o == null)
        return false;
    int mask = elements.length - 1;
    int i = head;
    Object x;
    while ( (x = elements[i]) != null) {
        if (o.equals(x)) {
            delete(i);
            return true;
        }
        i = (i + 1) & mask;
    }
    return false;
}

public boolean removeLastOccurrence(Object o) {
    if (o == null)
        return false;
    int mask = elements.length - 1;
    int i = (tail - 1) & mask;
    Object x;
    while ( (x = elements[i]) != null) {
        if (o.equals(x)) {
            delete(i);
            return true;
        }
        i = (i - 1) & mask;
    }
    return false;
}
```

队列方法：

```java
public boolean add(E e) {
    addLast(e);
    return true;
}

public boolean offer(E e) {
    return offerLast(e);
}

public E remove() {
    return removeFirst();
}

public E poll() {
    return pollFirst();
}

public E element() {
    return getFirst();
}

public E peek() {
    return peekFirst();
}
```

栈方法：

```java
public void push(E e) {
    addFirst(e);
}

public E pop() {
    return removeFirst();
}
```

删除特定索引的元素：

```java
private boolean delete(int i) {
    checkInvariants();
    final Object[] elements = this.elements;
    final int mask = elements.length - 1;
    final int h = head;
    final int t = tail;
    final int front = (i - h) & mask;
    final int back  = (t - i) & mask;

    // Invariant: head <= i < tail mod circularity
    if (front >= ((t - h) & mask))
        throw new ConcurrentModificationException();

    // Optimize for least element motion
    if (front < back) {
        if (h <= i) {
            System.arraycopy(elements, h, elements, h + 1, front);
        } else { // Wrap around
            System.arraycopy(elements, 0, elements, 1, i);
            elements[0] = elements[mask];
            System.arraycopy(elements, h, elements, h + 1, mask - h);
        }
        elements[h] = null;
        head = (h + 1) & mask;
        return false;
    } else {
        if (i < t) { // Copy the null tail as well
            System.arraycopy(elements, i + 1, elements, i, back);
            tail = t - 1;
        } else { // Wrap around
            System.arraycopy(elements, i + 1, elements, i, mask - i);
            elements[mask] = elements[0];
            System.arraycopy(elements, 1, elements, 0, t);
            tail = (t - 1) & mask;
        }
        return true;
    }
}

private void checkInvariants() {
    assert elements[tail] == null;
    assert head == tail ? elements[head] == null :
    (elements[head] != null &&
     elements[(tail - 1) & (elements.length - 1)] != null);
    assert elements[(head - 1) & (elements.length - 1)] == null;
}
```

集合方法：

```java
public int size() {
    return (tail - head) & (elements.length - 1);
}

public boolean isEmpty() {
    return head == tail;
}

public boolean contains(Object o) {
    if (o == null)
        return false;
    int mask = elements.length - 1;
    int i = head;
    Object x;
    while ( (x = elements[i]) != null) {
        if (o.equals(x))
            return true;
        i = (i + 1) & mask;
    }
    return false;
}

public boolean remove(Object o) {
    return removeFirstOccurrence(o);
}

public void clear() {
    int h = head;
    int t = tail;
    if (h != t) { // clear all cells
        head = tail = 0;
        int i = h;
        int mask = elements.length - 1;
        do {
            elements[i] = null;
            i = (i + 1) & mask;
        } while (i != t);
    }
}

public Object[] toArray() {
    return copyElements(new Object[size()]);
}

public <T> T[] toArray(T[] a) {
    int size = size();
    if (a.length < size)
        a = (T[])java.lang.reflect.Array.newInstance(
        a.getClass().getComponentType(), size);
    copyElements(a);
    if (a.length > size)
        a[size] = null;
    return a;
}
```

Object方法：

```java
public ArrayDeque<E> clone() {
    try {
        @SuppressWarnings("unchecked")
        ArrayDeque<E> result = (ArrayDeque<E>) super.clone();
        result.elements = Arrays.copyOf(elements, elements.length);
        return result;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError();
    }
}
```

