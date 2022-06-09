package org.example;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Random;

public class Randoms {
    public static final Random random = new Random();
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static int nextInt() {
        return random.nextInt();
    }

    public static float nextFloat() {
        return random.nextFloat();
    }

    public static double nextDouble() {
        return random.nextDouble();
    }

    public static boolean nextBoolean() {
        return random.nextBoolean();
    }

    public static long nextLong() {
        return random.nextLong();
    }

    public static String nextString() {
        return nextString(random.nextInt(30));
    }

    public static String nextString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static <E> E nextEnum(Class<E> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }


    @SneakyThrows
    public static <T> T nextObject(Class<T> clazz) {
        T t = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(t, nextBase(field.getType()));
        }
        return t;
    }

    /**
     * 基本类型加String
     */
    @SuppressWarnings("all")
    public static <T> T nextBase(Class<T> clazz) {
        if (clazz.equals(Long.class)) {
            return (T) Long.valueOf(nextLong());
        }
        if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(nextInt());
        }
        if (clazz.equals(Float.class)) {
            return (T) Float.valueOf(nextFloat());
        }
        if (clazz.equals(String.class)) {
            return (T) nextString();
        }
        if (clazz.equals(Double.class)) {
            return (T) Double.valueOf(nextDouble());
        }
        if (clazz.equals(Enum.class)) {
            return (T) nextEnum(clazz);
        }
        return null;
    }
}
