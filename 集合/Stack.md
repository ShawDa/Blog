### 1、概述

Stack 实现了一个后进先出的栈，它继承实现了 Vector 中的五种方法，包括 `push`、`pop`、`peak`、`empty` 和 `search`。当一个 Stack 被新建时，它其中不包括任何东西。`Deque` 实现了一个更完整的 `LIFO` 的栈，应该优先于这个类来使用：`Deque<Integer> stack = new ArrayDeque<Integer>();`。

```java
public class Stack<E> extends Vector<E>
```



### 2、核心方法

```java
public E push(E item) {  // 在最后添加一个元素
    addElement(item);  // synchronized方法

    return item;
}

public synchronized E pop() {  // 栈顶元素出栈
    E obj;
    int len = size();

    obj = peek();
    removeElementAt(len - 1);

    return obj;
}

public synchronized E peek() {  // 获取栈顶元素，也就是最后一个
    int len = size();

    if (len == 0)
        throw new EmptyStackException();
    return elementAt(len - 1);
}

public boolean empty() {  // 判空
    return size() == 0;
}

public synchronized int search(Object o) {  // 查找对象首次出现的位置
    int i = lastIndexOf(o);

    if (i >= 0) {
        return size() - i;
    }
    return -1;
}
```

