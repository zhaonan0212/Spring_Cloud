package com.delta.Duck;

public class RedDuck extends Duck {
    @Override
    Fly fly() {
        return new NoCanFly();
    }

    @Override
    Sound sound() {
        return new ZhiZhiSound();
    }
}
