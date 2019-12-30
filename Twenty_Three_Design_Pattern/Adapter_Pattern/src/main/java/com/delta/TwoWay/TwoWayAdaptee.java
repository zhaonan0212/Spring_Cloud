package com.delta.TwoWay;

/*
  这才是适配器的核心代码
  当我们想用一个接口，但是没法使用的时候，就再建一个类，将目标接口作为成员变量，然后实现适配者接口，重写里面的方法，即调用目标接口的方法
 */
public class TwoWayAdaptee implements TwoWayTarget,TwoWayAdapter {

    private TwoWayTarget target;
    private TwoWayAdapter adapter;

    public TwoWayAdaptee(TwoWayTarget target) {
        this.target = target;
    }

    public TwoWayAdaptee(TwoWayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void specificRequest() {
        target.request();
    }

    @Override
    public void request() {
        adapter.specificRequest();
    }
}
