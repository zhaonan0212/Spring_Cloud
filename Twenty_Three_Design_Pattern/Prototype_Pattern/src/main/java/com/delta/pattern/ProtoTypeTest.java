package com.delta.pattern;


public class ProtoTypeTest {
    public static void main(String[] args) {
        ProtoTypeManager pm = new ProtoTypeManager();
        Shape circle = pm.getShape("Circle");
        circle.countArea();

        Shape square = pm.getShape("Square");
        square.countArea();
    }
}
