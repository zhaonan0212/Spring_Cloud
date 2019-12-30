package com.delta.AbstractFactory;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        AbstractFactory farmersFactory =new FarmersFactory();
        farmersFactory.createDrink().price();
        farmersFactory.createNoodles().desc();


        KFCFactory kfcFactory =new KFCFactory();
        kfcFactory.createDrink().price();
        kfcFactory.createNoodles().desc();
    }
}
