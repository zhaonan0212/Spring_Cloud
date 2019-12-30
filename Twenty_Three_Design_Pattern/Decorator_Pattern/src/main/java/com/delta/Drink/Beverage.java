package com.delta.Drink;

/*
   1.父类，作为一个抽象类呈现
   2.装饰者模式：
      通常情况下，扩展一个类的功能会使用继承的方法，但继承具有静态特性，耦合度高，并且随着扩展功能的增多，子类会 膨胀，
      如果使用组合关系来创建一个包装对象，来包裹真实对象，并保持真是对象的结构不变的前提下，提供额外的功能
   3.应用场景：
      当需要给一个现有类添加附属职责，而且又不能采用生成子类进行扩充
      当需要通过对现有的一组基本功能进行排列组合而产生非常多的功能时，采用继承很难实现，
      对象的功能可以动态的添加，也可以动态的撤销
   4.装饰着的四部分：
      component 抽象构件
      concrete component 具体构件
      decorator 抽象装饰
      concrete decorator 具体装饰

      在本demo中，Beverage饮料就是抽象构建，DarkRoast，Espresso都是具体构建
      condimentDecorator是抽象装饰，mocha，soy是具体装饰
 */
public abstract class Beverage {
    String descriptionString = "Unknown Beverage";


    public String getDescriptionString() {
        return descriptionString;
    }

    //子类需要实现的抽象方法
    public abstract double cost();

}
