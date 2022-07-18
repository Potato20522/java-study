package com.potato.ch02;

import java.io.File;

class Order {
    public int id;
    public static String name;
}

public class NewTest {
    //1.创建指令
    public void newInstance() {
        Object object = new Object();
        File file = new File("a.txt");
    }

    public void newArray() {
        int[] ints = new int[10];
        Object[] objects = new Object[10];
        int[][] mints = new int[10][10];
        String[][] strArray = new String[10][];
    }

    //2.字段访问指令
    public void sayHello() {
        System.out.println("hello");
    }

    public void setOrderId() {
        Order order = new Order();
        order.id = 1001;
        System.out.println(order.id);

        Order.name = "ORDER";
        System.out.println(Order.name);
    }

    //3.数组操作指令
    public void setArray() {
        int[] ints = new int[10];
        ints[3] = 20;
        System.out.println(ints[1]);
    }

    public void arrayLength() {
        double[] arr = new double[10];
        System.out.println(arr.length);
    }

    //4.类型检查指令
    public String checkCast(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return null;
        }
    }

}
