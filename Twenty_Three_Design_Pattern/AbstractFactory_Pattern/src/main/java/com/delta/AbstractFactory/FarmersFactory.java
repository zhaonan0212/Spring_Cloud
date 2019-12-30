package com.delta.AbstractFactory;

public class FarmersFactory extends  AbstractFactory {
    @Override
    public Drink createDrink() {
        return new WhiteWater();
    }

    @Override
    public Noodles createNoodles() {
        return new LanzhouNoodles();
    }
}
