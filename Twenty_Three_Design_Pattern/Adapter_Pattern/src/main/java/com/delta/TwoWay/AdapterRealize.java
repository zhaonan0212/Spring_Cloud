package com.delta.TwoWay;

public class AdapterRealize implements TwoWayAdapter {
    @Override
    public void specificRequest() {
        System.out.println("适配器代码被调用");
    }
}
