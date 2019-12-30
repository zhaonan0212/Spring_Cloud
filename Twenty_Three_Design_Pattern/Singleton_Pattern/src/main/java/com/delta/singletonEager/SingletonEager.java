package com.delta.singletonEager;

public class SingletonEager {

    private static SingletonEager sig = new SingletonEager();

    private SingletonEager(){};

    public static SingletonEager getInstance(){
        return  sig;
    }
}

