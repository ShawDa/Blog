package cn.shawda.testreflect;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Person {
    public String publicString;
    protected String protectedString;
    String defaultString;
    private String privateString;

    private String name;
    private int age;

    public Person() {
    }

    private Person(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String eat() {
        return "eat";
    }

    private String eat(String fruit) {
        return "eat " + fruit;
    }

    public String eat(String fruit, int num) {
        return "eat " + num + " " + fruit;
    }
}
