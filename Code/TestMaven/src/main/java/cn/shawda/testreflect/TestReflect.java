package cn.shawda.testreflect;

import java.lang.reflect.Field;

public class TestReflect {
    public static void main(String[] args)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
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

        // 2 获取Field成员变量
        // 2.1 获取所有public成员变量
        Field[] publicFields = forNamePersonClass.getFields();
        for (Field publicField : publicFields) {
            System.out.println("public field = " + publicField);  // public java.lang.String cn.shawda.testreflect.Person.publicString
        }

        // 2.2 根据name获得Field，只能是public的，其他的或不存在会抛异常
        Field publicField = forNamePersonClass.getField("publicString");
        System.out.println(publicField);  // public java.lang.String cn.shawda.testreflect.Person.publicString

        // 2.3 获取该变量的值并设置值
        Person person1 = new Person();
        Object value1 = publicField.get(person1);
        System.out.println("value1 = " + value1);  // null
        publicField.set(person1, "shawda");
        System.out.println("set value1 = " + person1.publicString);  // shawda

        // 2.4 获取所有成员变量，不考虑修饰符
        Field[] declaredFields = forNamePersonClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
//            declaredField : public java.lang.String cn.shawda.testreflect.Person.publicString
//            declaredField : protected java.lang.String cn.shawda.testreflect.Person.protectedString
//            declaredField : java.lang.String cn.shawda.testreflect.Person.defaultString
//            declaredField declaredField: private java.lang.String cn.shawda.testreflect.Person.privateString
            System.out.println("declaredField : " + declaredField);
        }

        // 2.5 根据name获得Field，暴力反射
        Field declaredField = forNamePersonClass.getDeclaredField("privateString");
        declaredField.setAccessible(true);
        Person person2 = new Person();
        Object value2 = declaredField.get(person2);
        System.out.println("value2 = " + value2);  // null
        declaredField.set(person2, "shawda2");
        System.out.println("set value2 = " + declaredField.get(person2));  // shawda2

        // 3 获取构造方法
        // 3.1
    }
}
