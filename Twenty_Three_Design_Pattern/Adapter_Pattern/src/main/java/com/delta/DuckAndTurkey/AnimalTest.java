package com.delta.DuckAndTurkey;

public class AnimalTest {
    public static void main(String[] args) {

        GreenDuck greenDuck = new GreenDuck();
        testDuck(greenDuck);
        System.out.println("这是真鸭子");



        WildTurkey wildTurkey = new WildTurkey();
        wildTurkey.fly();
        wildTurkey.gobble();
        System.out.println("这是真鸡");

        TurkeyAdapter adapter = new TurkeyAdapter(wildTurkey);
        testDuck(adapter);
        System.out.println("这是鸡做的鸭子");
    }

    static void testDuck(Duck duck){
        duck.fly();
        duck.quack();
    }
}
