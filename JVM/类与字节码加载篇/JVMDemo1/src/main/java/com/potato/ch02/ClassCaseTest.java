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
    }

}
