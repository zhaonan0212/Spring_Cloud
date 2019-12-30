package com.delta.StaticProxy;

public class StaticProxyTest {
    public static void main(String[] args) {

        BuyHouse buyHouse = new BuyHouseImpl();
        buyHouse.buyHouse();
        System.out.println("这是我自己买==========");

        BuyHouseProxy buyHouseProxy = new BuyHouseProxy(buyHouse);
        buyHouseProxy.buyHouse();
        System.out.println("这是中介帮我买的===========");
    }
}
