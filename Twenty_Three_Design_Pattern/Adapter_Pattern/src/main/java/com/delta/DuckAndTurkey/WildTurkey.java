package com.delta.DuckAndTurkey;

public class WildTurkey implements Turkey {
    @Override
    public void fly() {
        System.out.println("我只能飞3米");
    }

    @Override
    public void gobble() {
        System.out.println("咯咯咯，咯咯咯");
    }
}
