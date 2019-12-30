package com.delta.Duck;

public class GreenDuck extends Duck {

    @Override
    Fly fly() {
        return new CanFly();
    }

    @Override
    Sound sound() {
        return new GuaGuaSound();
    }
}
