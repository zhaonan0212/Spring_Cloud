package com.delta.static_factory;

public class SimpleFactoryTest {
    public static void main(String[] args) {
        Noodles noodle = SimpleNoodlesFactory.createNoodle(1);
        noodle.desc();
    }
}
