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