package com.delta.Drink;

/*
    这个是奶泡类
 */
public class Whip extends CondimentDecorator {

    public Beverage beverage;

    private double myCost = 20;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescriptionString() {
        return beverage.getDescriptionString()+".Whip";
    }

    @Override
    public double cost() {
        return myCost+beverage.cost();
    }
}
