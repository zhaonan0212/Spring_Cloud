package com.delta.Drink;

public class DarkRoast extends  Beverage {

    public DarkRoast(){
        descriptionString = "DarkRoast";
    }

    @Override
    public double cost() {
        return 10;
    }
}
