package com.delta.innerClass;

public class InnerSingleton {

    private InnerSingleton(){};

    private static class SingletonHolder{
        private static InnerSingleton instance = new InnerSingleton();
    }

    public static InnerSingleton getInstance(){
        return SingletonHolder.instance;
    }
}
