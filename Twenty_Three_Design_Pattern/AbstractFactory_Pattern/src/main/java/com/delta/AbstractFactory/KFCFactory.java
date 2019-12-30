package com.delta.AbstractFactory;

public class KFCFactory extends AbstractFactory {
    @Override
    public Drink createDrink() {
        return new ColaWater();
    }

    @Override
    public Noodles createNoodles() {
        return new ZhajiangNoodles();
    }
}
