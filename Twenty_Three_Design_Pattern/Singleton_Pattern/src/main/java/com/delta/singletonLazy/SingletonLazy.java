package com.delta.singletonLazy;

public class SingletonLazy {
    public static void main(String[] args) {
        President pre1 = President.getInstance();
        pre1.getName();

        President pre2 = President.getInstance();
        pre2.getName();

        if (pre1 == pre2) {
            System.out.println("他们是同一个人");
        } else {
            System.out.println("他们是两个人");
        }
    }
}
