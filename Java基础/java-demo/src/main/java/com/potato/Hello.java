package com.potato;

import java.util.function.LongToDoubleFunction;

public class Hello {
    public static void main(String[] args) {
        Hello hello = new Hello();
        hello.aa(2222, hello::bb);
        hello.bb(i->1.0);

    }

    public void aa(int a, Callback<Integer> callback) {
        System.out.println("a：" + a);
        callback.accept(100);
    }

    //回调函数
    public void bb(int i) {
        System.out.println("回调函数接受到的参数"+i);
    }

    public void bb(LongToDoubleFunction function) {
        double v = function.applyAsDouble(2);
        System.out.println(v);
    }
}
