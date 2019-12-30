package com.delta.StaticProxy;

public class BuyHouseProxy implements BuyHouse {

    private BuyHouse buyHouse;

    public BuyHouseProxy(BuyHouse buyHouse) {
        this.buyHouse = buyHouse;
    }

    @Override
    public void buyHouse() {
        System.out.println("买房前准备资料");
        buyHouse.buyHouse();
        System.out.println("买后装修");
    }

    /*
    客户不想或者不能直接引用一个对象,所以需要找一个代理类,中介就可以啦
     */
}
