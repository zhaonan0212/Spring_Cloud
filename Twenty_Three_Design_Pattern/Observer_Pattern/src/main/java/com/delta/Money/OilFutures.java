package com.delta.Money;

import java.util.Observable;

/*
  author:
  description:原油期货类,
 */
public class OilFutures extends Observable {
    private float price;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        super.setChanged();
        super.notifyObservers(price);                //价格改变后,通知观察者
        this.price = price;
    }
}
