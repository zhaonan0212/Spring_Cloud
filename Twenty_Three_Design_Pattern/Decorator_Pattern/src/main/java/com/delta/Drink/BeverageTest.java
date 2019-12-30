package com.delta.Drink;

public class BeverageTest {
    public static void main(String[] args) {

        //现有一个底层的饮料，再填加调料
        Beverage beverage = new HouseBlend();
        System.out.println(beverage.getDescriptionString() + beverage.cost());

        //底层饮料添加 摩卡，摩卡和奶酪
        Beverage beverage1 = new DarkRoast();
        beverage1 = new Mocha(beverage1);
        beverage1 = new Mocha(beverage1);
        beverage1 = new Whip(beverage1);
        System.out.println(beverage1.getDescriptionString() + beverage1.cost());


        Beverage beverage2 = new Espresso();
        beverage2 = new Soy(beverage2);
        beverage2 = new Mocha(beverage2);
        beverage2 = new Whip(beverage2);
        System.out.println(beverage2.getDescriptionString() + beverage2.cost());

    }
}
