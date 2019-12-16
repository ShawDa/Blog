package cn.shawda.testreflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflect {
    public static void main(String[] args)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
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
        // 3.1 获取所有public构造方法
        Constructor[] constructors = forNamePersonClass.getConstructors();
        for (Constructor constructor : constructors) {
//            public cn.shawda.testreflect.Person()
//            public cn.shawda.testreflect.Person(java.lang.String,int)
            System.out.println(constructor);
        }

        // 3.2 获取有参构造方法
        Constructor constructorWithParams = forNamePersonClass.getConstructor(String.class, int.class);
        System.out.println("constructorWithParams = " + constructorWithParams);  // public cn.shawda.testreflect.Person(java.lang.String,int)
        Object person3 = constructorWithParams.newInstance("shawda3", 1024);
        System.out.println("person3 = " + person3);  // Person(publicString=null, protectedString=null, defaultString=null, privateString=null, name=shawda3, age=1024)

        // 3.3 获取无参构造方法
        Constructor constructorWithoutParams = forNamePersonClass.getConstructor();
        System.out.println("constructorWithoutParams = " + constructorWithoutParams);  // public cn.shawda.testreflect.Person()
        Object person4 = constructorWithoutParams.newInstance();
        System.out.println("person4 = " + person4);  // Person(publicString=null, protectedString=null, defaultString=null, privateString=null, name=null, age=0)

        // 3.4 获取私有构造方法
        Constructor privateConstructor = forNamePersonClass.getDeclaredConstructor(String.class);
        privateConstructor.setAccessible(true);
        System.out.println("privateConstructor = " + privateConstructor);  // private cn.shawda.testreflect.Person(java.lang.String)
        Object person5 = privateConstructor.newInstance("shawda5");
        System.out.println("person5 = " + person5);  // Person(publicString=null, protectedString=null, defaultString=null, privateString=null, name=shawda5, age=0)

        // 4 获取方法
        // 4.1 获取所有public方法
        Method[] methods = forNamePersonClass.getMethods();
        for (Method method : methods) {
            System.out.println("method = " + method);  // 获取的方法包括自己的(注解里的也有)和继承父类的
        }

        // 4.2 获取有参方法
        Method methodWithParams = forNamePersonClass.getMethod("eat", String.class, int.class);
        Person person6 = new Person();
        Object retValue6 = methodWithParams.invoke(person6, "apple", 2);
        System.out.println("retValue6 = "  + retValue6);  // eat 2 apple

        // 4.3 获取无参方法
        Method methodWithoutParams = forNamePersonClass.getMethod("eat");
        Person person7 = new Person();
        Object retValue7 = methodWithoutParams.invoke(person7);
        System.out.println("retValue7 = "  + retValue7);  // eat

        // 4.4 获取私有方法
        Method privateMethod = forNamePersonClass.getDeclaredMethod("eat", String.class);
        privateMethod.setAccessible(true);
        Person person8 = new Person();
        Object retValue8 = privateMethod.invoke(person8, "orange");
        System.out.println("retValue8 = "  + retValue8);  // eat orange
    }
}
