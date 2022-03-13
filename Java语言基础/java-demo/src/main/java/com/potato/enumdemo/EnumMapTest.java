package com.potato.enumdemo;

import java.util.EnumMap;
import java.util.Map;

public class EnumMapTest {
    public enum Color {
        RED, BLUE, BLACK, YELLOW, GREEN;
    }

    public static void main(String[] args) {
        EnumMap<Color, String> map = new EnumMap<>(Color.class);
        map.put(Color.YELLOW, "黄色");
        map.put(Color.RED, "红色");
        map.put(Color.BLUE, null);
        //        map.put(null, "无");   //会报NullPonitException的错误
        map.put(Color.BLACK, "黑色");
        map.put(Color.GREEN, "绿色");

        for (Map.Entry<Color, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println(map);
    }
}