package com.delta.innerClass;

public class Demo1 {
    public static void main(String[] args) {
        InnerSingleton ins1 = InnerSingleton.getInstance();
        InnerSingleton ins2 = InnerSingleton.getInstance();
        System.out.println(ins1);
        System.out.println(ins2);

    }
}
