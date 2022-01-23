package com.potato.value;

/**
 * Java中只有值传递
 * 一个方法不能修改一个基本数据类型的参数
 */
public class ValueDemo {
    public static void main(String[] args) {
        int num1 = 10;
        int num2 = 20;
        swap(num1, num2);
        System.out.println("num1 = " + num1);
        System.out.println("num2 = " + num2);
    }

    public static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }

    public static void change(int[] array) {
        // 将数组的第一个元素变为0
        array[0] = 0;
    }

    public static void swap(Person person1, Person person2) {
        Person temp = person1;
        person1 = person2;
        person2 = temp;
        System.out.println("person1:" + person1.getName());
        System.out.println("person2:" + person2.getName());
    }
}

class Person {
    private String name;
    // 省略构造函数、Getter&Setter方法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
