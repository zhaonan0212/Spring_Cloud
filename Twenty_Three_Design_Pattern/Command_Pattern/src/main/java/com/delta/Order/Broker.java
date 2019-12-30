package com.delta.Order;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.List;
/*
   将一个请求封装成一个对象，从而使您可以用不同的请求对客户进行参数化
   首先创建命令接口Order，请求的Stock类，实体命令类BuyStock和SellStock，都实现Order接口，将执行实际的命令处理
   创建作为对象的类Broker，它接受订单并下单。
 */
public class Broker {
    private List<Order> orderList = new ArrayList<Order>();



    public void takeOrder(Order order){
        orderList.add(order);
    }

    public void placeOrders(){
        for (Order order : orderList) {
            order.execute();
        }
        orderList.clear();
    }
}
