package cn.shawda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add(null);
        Queue<Integer> queue = new LinkedList<>();
        Collections.addAll(queue, 1, 2, 3);  // [1, 2, 3]
        queue.add(4);  // [1, 2, 3, 4]
        queue.offer(5);  // [1, 2, 3, 4, 5]
        queue.remove();  // [2, 3, 4, 5]
        queue.poll();  // [3, 4, 5]
        System.out.println(queue.element());  // 3 [3, 4, 5]
        System.out.println(queue.peek());  // 3 [3, 4, 5]
        System.out.println(queue);
        Map<String, Integer> map = new HashMap<>();
        map.put("3", 1);
        map.put("1", 3);
        map.put("2", 5);  // {1=3, 2=5, 3=1}
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Map.Entry.comparingByValue());  // [3=1, 1=3, 2=5]
    }
}

class Generic<T> {
    private T name;

    public Generic(T name) {
        this.name = name;
    }

    public T getName() {
        return name;
    }
}

interface Generator<T> {


    int size();

    T add(T element);

    T remove(int index);
}

// 未传入泛型实参时，类也要申明泛型，否则编译报错：class MyGenerator implements Generator<T>
class MyGenerator<T> implements Generator<T> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public T add(T element) {
        return null;
    }

    @Override
    public T remove(int index) {
        return null;
    }
}

// 传入实参时，方法的泛型参数也要申明类型
class StringGenerator implements Generator<String> {
    private List<String> stringList = Arrays.asList("a", "b", "c");

    @Override
    public int size() {
        return stringList.size();
    }

    @Override
    public String add(String element) {
        stringList.add(element);
        return element;
    }

    @Override
    public String remove(int index) {
        return stringList.remove(index);
    }
}

