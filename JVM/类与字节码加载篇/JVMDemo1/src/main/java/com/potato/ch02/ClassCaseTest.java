package com.potato.ch02;

import org.junit.jupiter.api.Test;

/**
 * 指令3：类型转换指令
 */
public class ClassCaseTest {
    //宽化类型转换
    public void upCast1() {
        int i = 10;
        long l = i;
        float f = i;
        double d = i;

        float f1 = l;
        double d1 = l;
        double d2 = f1;
    }

    //举例：精度损失问题
    @Test
    void upCast2() {
        int i = 123123123;
        float f = i;
        System.out.println(f);//123123120

        //long l = 123123123123L;
        long l = 123123123123123L;
        double d = l;
        System.out.println(d);//123123123123120
    }

    //byte short等，看作int
    void upCast3(byte b) {
        int i = b;
        long l = b;
        double d = b;
    }

    void upCast4(short b) {
        int i = b;
        long l = b;
        double d = b;
    }

    //窄化类型转换
    void downCast1() {
        int i = 10;
        byte b = (byte) i;
        short s = (short) i;
        char c = (char) i;

        long l = 10L;
        int i1 = (int) l;
        byte b1 = (byte) l;//字节码分两步骤：先long转int,再int转byte
    }

    void downCast2() {
        float f = 10;
        long l = (long) f;
        int i = (int) f;
        byte b = (byte) f;//也是分两步

        double d = 10;
        byte b1 = (byte) d;//也是分两步
    }

    void downCast3() {
        short s = 10;
        byte b = (byte) s;//i2b
    }

    //窄化类型转换的精度损失
    @Test
    void downCast4() {
        int i = 128;
        byte b = (byte) i;
        System.out.println(b);//-128
    }

    //NaN
    @Test
    void downCast5() {
        double d1 = Double.NaN;
        int i = (int) d1;
        System.out.println(i);//0

        double d2 = Double.NaN;
        long l = (long) d2;
        int j = (int) d2;
        System.out.println(l);//0
        System.out.println(j);//0
    }

    //无穷大
    @Test
    void downCast6() {
        double d1 = Double.POSITIVE_INFINITY;
        long l = (long) d1;
        int j = (int) d1;
        float k = (float) d1;
        System.out.println(l);//9223372036854775807 long max
        System.out.println(j);//2147483647 int max
        System.out.println(k);//Infinity
    }
}
