### 1、概述

ArrayList 是 List 接口的动态可变长数组实现，它实现了 List 的所有方法并允许包括 null 在内的所有元素。为了实现这些方法，ArrayList 提供方法去操作被用来真正存储元素的数组的大小。ArrayList 类基本和 Vector 类等同，除了它不是线程安全的。

每个 ArrayList 都有一个容量，它指的是用来存储元素的数组的大小，总是大于等于它的元素个数。随着向 ArrayList 中不断添加元素，其容量也会增长，这就会带来数据向新数组中的拷贝。所以，若知道数据量的大小就可以在创建 ArrayList 时指定其容量，这样就可以减少多次扩容带来的性能损耗。



### 2、继承类与实现接口

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

> * 继承 AbstractList 类，实现 List 接口，是一个数组队列
> * 实现 RandomAccess 接口，提供了随机访问的功能
> * 实现了 Cloneable、java.io.Serializable 接口，能被 clone，支持序列化



### 3、变量

```java
private static final int DEFAULT_CAPACITY = 10;  // 默认初始化容量
private static final Object[] EMPTY_ELEMENTDATA = {};  // 默认空数组
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
// 存储数据的数组，每个位置不一定都有数据，transient避免序列化，非private简化嵌套类访问
transient Object[] elementData; // non-private to simplify nested class access
private int size;  // 列表大小
// 继承自AbstractList，修改次数（add、remove或容量变化时+1）
protected transient int modCount = 0;
// 分配的最大数组容量，一些VMs会在数组里保留些头字段，尝试分配更多可能导致OutOfMemoryError
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```



### 4、构造方法

```java
// 不带参数初始化为默认空数组
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

// 带初始容量大小
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
}

// 带初始化集合，JDK bug导致toArray()方法不一定返回Object[]
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```



### 5、重要方法

`trimToSize()`：将 ArrayList 的容量修建至当前大小，以此来最小化实例的存储。

```java
public void trimToSize() {
    modCount++;  // 修改次数+1
    if (size < elementData.length) {
        elementData = (size == 0)
                ? EMPTY_ELEMENTDATA
                : Arrays.copyOf(elementData, size);
    }
}
```

`ensureCapacity(int minCapacity)`：在需要的时候增加 ArrayList 的容量来保证它至少可以容纳 minCapacity 指定的元素数量，内部方法主要是在增加元素的相关 add 方法中使用。

```java
// 对外提供的方法，初始化为空时最小扩容容量为10，否则为0
public void ensureCapacity(int minCapacity) {
    int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
        // any size if not default element table
        ? 0
        // larger than default for default empty table. It's already
        // supposed to be at default size.
        : DEFAULT_CAPACITY;

    if (minCapacity > minExpand) {  // 当minCapacity大于最小扩容容量时才扩容
        ensureExplicitCapacity(minCapacity);
    }
}

// 内部方法，初始化为空时，至少扩容为10
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code 可能会溢出的代码
    if (minCapacity - elementData.length > 0)  // 判断容量是否足够
        grow(minCapacity);
}
```

#### grow(minCapacity)

具体的扩容方法：

```java
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);  // 扩容为原来的1.5倍(-1)
    if (newCapacity - minCapacity < 0)  // 如果比指定的容量小就用指定容量
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)  // 如果比最大长度大就按照指定容量来
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

// 指定容量比最大长度大就返回Integer.MAX_VALUE，否则返回最大长度
private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

`indexOf(Object o)`：两个获得对象索引的方法。

```java
public int indexOf(Object o) {
    if (o == null) {  // 注意这里要判空
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}

public int lastIndexOf(Object o) {
    if (o == null) {  // 注意这里要判空
        for (int i = size-1; i >= 0; i--)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = size-1; i >= 0; i--)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```

`clone()`：对外的克隆方法。

```java
public Object clone() {
    try {
        ArrayList<?> v = (ArrayList<?>) super.clone();
        v.elementData = Arrays.copyOf(elementData, size);  // 剪枝了下
        v.modCount = 0;  // 修改次数置为0
        return v;
    } catch (CloneNotSupportedException e) {
        // this shouldn't happen, since we are Cloneable
        throw new InternalError(e);
    }
}
```

`toArray()`：转成数组的方法。

```
public Object[] toArray() {
    return Arrays.copyOf(elementData, size);
}

public <T> T[] toArray(T[] a) {
    if (a.length < size)
        // Make a new array of a's runtime type, but my contents:
        return (T[]) Arrays.copyOf(elementData, size, a.getClass());
    System.arraycopy(elementData, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;  // 只有当ArrayList中没有null时好用来确定它的长度
    return a;
}
```

#### get(int index)

根据 index 来获取数据。

```java
public E get(int index) {
    rangeCheck(index);  // 先检查范围
    return elementData(index);
}

private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

E elementData(int index) {
    return (E) elementData[index];
}
```