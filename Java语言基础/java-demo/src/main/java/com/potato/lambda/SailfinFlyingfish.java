package com.potato.lambda;

class SailfinFlyingfish implements Swimmable, Flyable {

    @Override
    public void fly() {
        System.out.println("*flap flap*");
    }

    @Override
    public void swim() {
        System.out.println("*swim swim*");
    }

    //基于泛型的交叉类型
    public static <T extends Flyable & Swimmable> void process(T animal) {
        animal.fly();
        animal.swim();
    }
}