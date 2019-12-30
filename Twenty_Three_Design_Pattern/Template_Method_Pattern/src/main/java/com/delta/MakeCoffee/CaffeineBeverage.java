package com.delta.MakeCoffee;

/*
   这就是泡茶和coffee的固定方法
   将固定方法放到父类中，抽象方法由子类去实现
 */
public abstract class CaffeineBeverage {

    public void prepareRecipe(){
        boilWater();   //相同
        brewCoffeeGrinds();
        pourInCup();   //相同
        addSugarAndMilk();
    }

    private void boilWater() {
        System.out.println("Boiling Water");
    }

    public abstract void brewCoffeeGrinds();

    private void pourInCup() {
        System.out.println("Pouring into cup");
    }

    public abstract void addSugarAndMilk();
}
