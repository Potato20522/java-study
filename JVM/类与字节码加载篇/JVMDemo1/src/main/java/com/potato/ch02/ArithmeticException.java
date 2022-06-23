package com.potato.ch02;

import org.junit.jupiter.api.Test;



/**
 * 指令2：算术指令
 */
public class ArithmeticException {

    @Test
    public void method1() {
        int i = 10;
        double j = i / 0.0;
        System.out.println(j);//Infinity

        double d1 = 0.0;
        double d2 = d1 / 0.0;
        System.out.println(d2);//NaN: not a number
    }

    public void method2() {
        float i = 10;
        float j = -i;
        i = -j;
    }

    public void method3() {
        int i = 100;
//        i = i + 10;
        i += 10;
    }

    public int method4() {
        int a = 80;
        int b = 7;
        int c = 10;
        return (a + b) * c;
    }

    public int method5(int i, int j) {
        return ((i + j - 1) & ~(j - 1));
    }

    //关于前(++)和后(++)
    public void method6() {
        int i = 10;
        i++;
    }

    public void method7() {
        int i = 10;
        int a = i++;

        int j = 20;
        int b = ++j;
    }

    @Test
    public void method8() {
        int i = 10;
        i = i++;
        System.out.println(i);//10
    }
}
