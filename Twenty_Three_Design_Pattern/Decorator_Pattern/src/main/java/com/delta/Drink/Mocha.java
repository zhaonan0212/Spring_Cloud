package com.delta.Drink;
/*
   这个是摩卡，
 */
public class Mocha extends CondimentDecorator {

    public Beverage beverage;

    private double myCost = 10;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescriptionString() {
        return beverage.getDescriptionString()+ ".Mocha";
    }

    @Override
    public double cost() {
        return myCost+beverage.cost();
    }
}
