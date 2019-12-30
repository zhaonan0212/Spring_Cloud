package com.delta.Travel;

public class Car implements Travel {
    @Override
    public void goOut() {
        System.out.println("我开车自驾游");
    }
}
