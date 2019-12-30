package com.delta.Roles;

public class RolesDemo {
    public static void main(String[] args) {
        Component component = new ConcreteConponent();
        component.operation();
        System.out.println("================");
        component = new ConcreteDecorator(component);
        component.operation();
    }
}
