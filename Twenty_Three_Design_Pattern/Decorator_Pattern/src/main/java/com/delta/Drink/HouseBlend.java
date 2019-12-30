package com.delta.Drink;

/*
  以这个为起点，开始往里面添加调料
 */
public class HouseBlend extends Beverage {


    public HouseBlend(){
        descriptionString="House Blend Coffee";
    }

    @Override
    public double cost() {
        return 10;
    }
}
