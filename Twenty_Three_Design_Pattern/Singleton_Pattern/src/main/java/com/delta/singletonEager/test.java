package com.delta.singletonEager;

public class test {
    public static void main(String[] args) {
        SingletonEager instance1 = SingletonEager.getInstance();
        SingletonEager instance2 = SingletonEager.getInstance();

        System.out.println(instance1);
        System.out.println(instance2);

        if (instance1 ==instance2){
            System.out.println("同一个对象");
        }

    }
}
