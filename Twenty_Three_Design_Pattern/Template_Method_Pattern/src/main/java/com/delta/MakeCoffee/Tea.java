package com.delta.MakeCoffee;

public class Tea extends CaffeineBeverage {

    @Override
    public void brewCoffeeGrinds() {
        System.out.println("Steeping the tea");
    }

    @Override
    public void addSugarAndMilk() {
        System.out.println("Adding Lemon");
    }
}
