package com.potato.enumdemo;

import java.util.Arrays;
import java.util.Collection;

public class App2 {
    public static void main(String[] args) {
        double x = 4;
        double y = 2;
        test(Arrays.asList(ExtendedOperation.values()), x, y);
    }

    private static void test(Collection<? extends Operation> opSet, double x, double y) {
        for (Operation op : opSet) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }
}