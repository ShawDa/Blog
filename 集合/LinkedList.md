### 1、概述

LinkedList 是 List 和 Deque 接口的双向链表实现，所有实现都不是同步的，非线程安全。

它的插入、删除操作比 ArrayList 更高效，随机访问的效率相比较更差。



### 2、继承类实现接口

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
```

LinkedList 继承自 AbstractSequenceList，实现了 List、Deque、Cloneable、java.io.Serializable 接口。AbstractSequenceList 提供了 List 接口骨干性的实现以减少实现 List 接口的复杂度，Deque 接口定义了双端队列的操作。

在 LinkedList 中，除了本身自己的方法外，还提供了一些可以使其作为栈、队列或者双端队列的方法。



### 3、变量

```java
transient int size = 0;  // 长度
transient Node<E> first;  // 头指针
transient Node<E> last;  // 尾指针

private static class Node<E> {  // 内部静态类
    E item;  // 元素值
    Node<E> next;  // 后继
    Node<E> prev;  // 前驱

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```



### 4、构造方法

```java
public LinkedList() {  // 构造一个空链表
}

public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);  // 根据c构造LinkedList
}

public boolean addAll(Collection<? extends E> c) {
    return addAll(size, c);
}

public boolean addAll(int index, Collection<? extends E> c) {
    checkPositionIndex(index);  // 检查范围

    Object[] a = c.toArray();
    int numNew = a.length;
    if (numNew == 0)  // 如果c为空就返回false
        return false;

    Node<E> pred, succ;  // 前驱和后继节点
    if (index == size) {  // 如果到最后add，那么前驱就是last，后继为null
        succ = null;
        pred = last;
    } else {  // 否则后继为index处节点，前驱为后继前一个
        succ = node(index);
        pred = succ.prev;
    }

    for (Object o : a) {
        @SuppressWarnings("unchecked") E e = (E) o;
        Node<E> newNode = new Node<>(pred, e, null);  // 这里确定了前驱节点，找后继节点就行
        if (pred == null) // 最前面添加就将newNode赋给first
            first = newNode;
        else  // 否则让前驱节点的后继为newNode
            pred.next = newNode;
        pred = newNode;
    }

    if (succ == null) {  // 后继为空说明last为最后添加的Node
        last = pred;
    } else {  // 否则将pred和succ连起来
        pred.next = succ;
        succ.prev = pred;
    }

    size += numNew;
    modCount++;
    return true;
}

private void checkPositionIndex(int index) {
    if (!isPositionIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

private boolean isPositionIndex(int index) {
    return index >= 0 && index <= size;
}
```



### 5、重要方法

`linkFirst(E e)`：将节点插入链表头。

```java
private void linkFirst(E e) {
    final Node<E> f = first;  // f记录之前的链表头
    final Node<E> newNode = new Node<>(null, e, f);  // 新节点，pre为空，next为f
    first = newNode;  // 新节点赋给first
    if (f == null)  // 空节点
        last = newNode;
    else
        f.prev = newNode;  // f前驱为新节点
    size++;
    modCount++;
}
```

`linkLast(E e)`：将节点插入链表尾。

```java
void linkLast(E e) {
    final Node<E> l = last;  // l记录尾节点
    final Node<E> newNode = new Node<>(l, e, null);  //新节点，pre为l，next为空
    last = newNode;  // 新节点赋给last
    if (l == null)  // 空节点
        first = newNode;
    else
        l.next = newNode;  // l后继为新节点
    size++;
    modCount++;
}
```

`linkBefore(E e, Node<E> succ)`：在非空节点 succ 之前插入 e。

```java
void linkBefore(E e, Node<E> succ) {
    // assert succ != null;
    final Node<E> pred = succ.prev;  // 先找到succ的前驱
    final Node<E> newNode = new Node<>(pred, e, succ);  // 建好新节点
    succ.prev = newNode;  // 将succ前驱改为新节点
    if (pred == null)  // 插在首尾
        first = newNode;
    else
        pred.next = newNode;  // 将pred的后继改为新节点
    size++;
    modCount++;
}
```

`unlinkFirst(Node<E> f)`：删除非空的头结点 f。

```java
private E unlinkFirst(Node<E> f) {
    // assert f == first && f != null;
    final E element = f.item;
    final Node<E> next = f.next;
    f.item = null;
    f.next = null; // help GC
    first = next;
    if (next == null)
        last = null;
    else
        next.prev = null;
    size--;
    modCount++;
    return element;
}
```

`unlinkLast(Node<E> l)`：删除非空的尾节点 l。

```java
private E unlinkLast(Node<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    final Node<E> prev = l.prev;
    l.item = null;
    l.prev = null; // help GC
    last = prev;
    if (prev == null)
        first = null;
    else
        prev.next = null;
    size--;
    modCount++;
    return element;
}
```

`unlink(Node<E> x)`：删除非空的节点 x。

```java
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```

#### public的get、add、remove方法

```java
public E getFirst() {
    final Node<E> f = first;
    if (f == null)
        throw new NoSuchElementException();
    return f.item;
}

public E getLast() {
    final Node<E> l = last;
    if (l == null)
        throw new NoSuchElementException();
    return l.item;
}

public E removeFirst() {
    final Node<E> f = first;
    if (f == null)
        throw new NoSuchElementException();
    return unlinkFirst(f);
}

public E removeLast() {
    final Node<E> l = last;
    if (l == null)
        throw new NoSuchElementException();
    return unlinkLast(l);
}

public void addFirst(E e) {
    linkFirst(e);
}

public void addLast(E e) {
    linkLast(e);
}

public boolean contains(Object o) {
    return indexOf(o) != -1;
}

public int size() {
    return size;
}

public boolean add(E e) {
    linkLast(e);
    return true;
}

public boolean remove(Object o) {
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) {
                unlink(x);
                return true;
            }
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
    }
    return false;
}

public void clear() {
    // Clearing all of the links between nodes is "unnecessary", but:
    // - helps a generational GC if the discarded nodes inhabit
    //   more than one generation
    // - is sure to free memory even if there is a reachable Iterator
    for (Node<E> x = first; x != null; ) {
        Node<E> next = x.next;
        x.item = null;
        x.next = null;
        x.prev = null;
        x = next;
    }
    first = last = null;
    size = 0;
    modCount++;
}

public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}

Node<E> node(int index) {
    // assert isElementIndex(index);

    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}

public E set(int index, E element) {
    checkElementIndex(index);
    Node<E> x = node(index);
    E oldVal = x.item;
    x.item = element;
    return oldVal;
}

public void add(int index, E element) {
    checkPositionIndex(index);

    if (index == size)
        linkLast(element);
    else
        linkBefore(element, node(index));
}

public E remove(int index) {
    checkElementIndex(index);
    return unlink(node(index));
}
```

`indexOf(Object o)`、`lastIndexOf(Object o)`：对象第一次和最后一次出现的位置。

```java
public int indexOf(Object o) {
    int index = 0;
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null)
                return index;
            index++;
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item))
                return index;
            index++;
        }
    }
    return -1;
}

public int lastIndexOf(Object o) {
    int index = size;
    if (o == null) {
        for (Node<E> x = last; x != null; x = x.prev) {
            index--;
            if (x.item == null)
                return index;
        }
    } else {
        for (Node<E> x = last; x != null; x = x.prev) {
            index--;
            if (o.equals(x.item))
                return index;
        }
    }
    return -1;
}
```

#### 队列操作