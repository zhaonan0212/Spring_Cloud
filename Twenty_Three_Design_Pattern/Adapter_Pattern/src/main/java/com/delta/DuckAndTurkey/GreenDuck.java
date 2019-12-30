package com.delta.DuckAndTurkey;

public class GreenDuck implements Duck{
    @Override
    public void fly() {
        System.out.println("飞了15米远");
    }

    @Override
    public void quack() {
        System.out.println("嘎嘎嘎，嘎嘎嘎");
    }
}
