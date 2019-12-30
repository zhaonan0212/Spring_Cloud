package com.delta.static_factory;

public class SimpleNoodlesFactory {

    public static final int TYPE_LZ = 1;
    public static final int TYPE_PM = 2;
    public static final int TYPE_ZJ = 3;

    /*
    它是一个具体的类,非接口抽象类,有一个重要的create方法,利用if或者switch创建产品并返回
    create方法通常是静态的,所以是静态工厂
     */
    public static Noodles createNoodle(int type) {
        switch (type) {
            case TYPE_LZ:
                return new Lanzhou_Noodles();
            case TYPE_PM:
                return new Programmer_pm();
            case TYPE_ZJ:
            default:
                return new ZhajiangNoodles();
        }
    }
    //缺点:1.扩展性差,如果想增加一种面条,除了新增面条类,还要修改工厂类的方法
    //    2.不同的产品需要不同的额外参数的时候不支持
}
