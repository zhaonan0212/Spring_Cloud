package com.delta.doubleSynchronized;

public class DoubleSyncSingleton {

    private DoubleSyncSingleton(){};

    private volatile static DoubleSyncSingleton instance;

    public static DoubleSyncSingleton getInstance(){
        if(instance == null){
            synchronized (DoubleSyncSingleton.class){
                if (instance == null ){
                    instance = new DoubleSyncSingleton();
                }
            }
        }
        return  instance;
    }
}
