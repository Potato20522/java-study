package com.potato.lambda;

public class Main {
    public static void main(String[] argv) {
        Calculator func = (NonFunction & Calculator) (x, y) -> x + y;
        System.out.println(func.calculate(1,2));

        NonFunction nonFunc = (NonFunction & Calculator) (x, y) -> x + y;
//        System.out.println(nonFunc.calculate(1,2));
    }
}

@FunctionalInterface
interface Calculator {
    long calculate(long x, long y);
}

interface NonFunction {
//    long calculate(long x, long y);
}