package com.delta.MakeCoffee;

public class Coffee extends CaffeineBeverage {
    @Override
    public void brewCoffeeGrinds() {
        System.out.println("Dripping Coffee through filter");
    }

    @Override
    public void addSugarAndMilk() {
        System.out.println("Adding sugar and milk");
    }


}
