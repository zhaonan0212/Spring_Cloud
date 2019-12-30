package com.delta.TwoWay;

/*
   1.适配器模式：将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而不能一起工作的那些类能一起工作
   2.优点：
      客户端通过适配器可以透明的调用目标接口
      复用了现有的接口。不需要修改源代码而重用现有的适配者类
      程序解耦，解决了目标类和适配者类接口不一致的问题
    3.adaptee 适配者
      adapter 适配器
    4.使用场景：
       前面开发的系统存在满足新系统的需求的类，但其接口同新街口不一致
       使用第三方的组件，但组件接口定义和自己要求的接口定义不同
 */
public class TwoWayTest {
    public static void main(String[] args) {
        System.out.println("双向通过适配器访问适配者");
        TwoWayAdapter adapter = new AdapterRealize();
        TwoWayTarget target = new TwoWayAdaptee(adapter);
        target.request();

        System.out.println("============");
        System.out.println("适配器通过双向适配器访问目标");
        target = new TargetRealize();
        adapter = new TwoWayAdaptee(target);
        adapter.specificRequest();
    }
}
