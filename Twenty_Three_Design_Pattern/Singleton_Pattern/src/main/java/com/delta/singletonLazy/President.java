package com.delta.singletonLazy;

/*
1.单例模式只有一个实例对象
2.该单例必须由单例类自行创建
3.单例类对外提供一个访问该单例的全局访问点
 */

//主要就是通过私有构造方法,导致外部类无法创建实例,只能自己构建
public class President {
    private static volatile President instance = null;    //volatile 保证instance在所有线程中同步,常用于保存内存可见性和防止指令重排序

    private President() {
        System.out.println("产生一个总统");
    }

    public static President getInstance() {
        if (instance == null) {
            instance = new President();
        } else {
            System.out.println("已经有一个总统,不能产生总统");
        }

        return instance;
    }

    public void getName(){
        System.out.println("总统名字叫特普朗");
    }

}
