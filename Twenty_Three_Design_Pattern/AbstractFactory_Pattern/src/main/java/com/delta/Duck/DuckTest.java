package com.delta.Duck;

public class DuckTest {
    public static void main(String[] args) {
        Duck duck = new GreenDuck();
        duck.fly().fly();
        duck.sound().sound();
    }
}
