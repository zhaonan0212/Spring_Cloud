package com.delta.Drink;

/*
    实现调料的父类，必须能够代替饮料类，所以继承饮料类，
    这里要明白一个事情：
       饮料 + 调料 = 星巴克
       饮料有：espresso（浓缩咖啡）HouseBlend（混合咖啡）
       调料有：Mocha（摩卡）whip（奶泡）Soy（豆浆）


 */
public abstract class CondimentDecorator extends  Beverage{

    //调料类必须实现的方法
    public abstract  String getDescriptionString();

}
