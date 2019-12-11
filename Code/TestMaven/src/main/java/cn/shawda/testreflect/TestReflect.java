package cn.shawda.testreflect;

public class TestReflect {
    public static void main(String[] args) throws ClassNotFoundException {
        // 1 获取Class对象
        // 1.1 通过类名
        Class personClass = Person.class;
        System.out.println("personClass = " + personClass);  // class cn.shawda.testreflect.Person

        // 1.2 通过对象
        Person person = new Person();
        Class newPersonClass = person.getClass();
        System.out.println("newPersonClass = " + newPersonClass);  // class cn.shawda.testreflect.Person

        // 1.3 forName方法
        Class forNamePersonClass = Class.forName("cn.shawda.testreflect.Person");
        System.out.println("forNamePersonClass = " + forNamePersonClass);  // class cn.shawda.testreflect.Person

        // 比较三个Class对象
        System.out.println((personClass == newPersonClass) + " " + (personClass == forNamePersonClass));  // true true
    }
}
