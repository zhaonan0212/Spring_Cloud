package com.delta.factroy_methods;

public class MoreMethodFactory {

    public static Noodles createLZ(){
        return new Lanzhou_Noodles();
    }

    public static Noodles createPM(){
        return  new Programmer_pm();
    }

    public static Noodles createZJ(){
        return  new ZhajiangNoodles();
    }
}
