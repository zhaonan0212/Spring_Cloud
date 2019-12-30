package com.delta.Duck;

public class NoCanFly extends Fly {
    @Override
    void fly() {
        System.out.println("我比较笨,飞不起来");
    }
}
