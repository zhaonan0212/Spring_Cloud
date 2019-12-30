package com.delta.Drink;

public class Espresso extends Beverage {

    public Espresso(){
        descriptionString = "Espresso";
    }

    @Override
    public double cost() {
        return 5;
    }
}
