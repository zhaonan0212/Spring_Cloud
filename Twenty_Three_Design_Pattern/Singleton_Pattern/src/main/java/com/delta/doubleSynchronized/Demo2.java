package com.delta.doubleSynchronized;

public class Demo2 {
    public static void main(String[] args) {
        DoubleSyncSingleton doubleSyncSingleton1 = DoubleSyncSingleton.getInstance();
        DoubleSyncSingleton  doubleSyncSingleton2= DoubleSyncSingleton.getInstance();
        System.out.println(doubleSyncSingleton1);
        System.out.println(doubleSyncSingleton2);

    }
}
