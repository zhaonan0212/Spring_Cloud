package com.delta.Drink;

public class Soy extends CondimentDecorator {

    public Beverage beverage;
    private double myCost = 25;

    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescriptionString() {
        return beverage.getDescriptionString()+".Soy";
    }

    @Override
    public double cost() {
        return beverage.cost()+myCost;
    }
}
