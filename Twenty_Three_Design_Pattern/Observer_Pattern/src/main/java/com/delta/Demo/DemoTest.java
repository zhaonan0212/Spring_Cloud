package com.delta.Demo;

public class DemoTest {
    public static void main(String[] args) {
        ProductionDemo productionDemo = new ProductionDemo();

        CurrentDisplay currentDisplay = new CurrentDisplay(productionDemo);

        productionDemo.setMeasurements("小张","123",1234);
        productionDemo.setMeasurements("小张1","123",1234);
        productionDemo.setMeasurements("小张2","123",1234);


/*        ProductionDemo productionDemo = new ProductionDemo("张飞","123",15);
        productionDemo.notifyObserver();

        ProductionDemo productionDemo1 = new ProductionDemo("赵云","1234",25);
        productionDemo1.notifyObserver();

        ProductionDemo productionDemo2 = new ProductionDemo("张飞1","1123",135);
        productionDemo2.notifyObserver();*/
    }
}
