package com.delta.Money;

import java.util.Observable;
import java.util.Observer;

/*
  author:
  description:多方类
 */
public class Bull implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        float price = ((Float) arg).floatValue();
        if (price > 0) {
            System.out.println("油价上涨"+price+"元，多方高兴了！");
        } else {
            System.out.println("油价下跌"+(-price)+"元，多方伤心了！");
        }
    }
}

