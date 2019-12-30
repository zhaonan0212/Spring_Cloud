package com.delta.Hook;

public class HookTest {
    public static void main(String[] args) {
        Tea tea = new Tea();
        Coffee coffee = new Coffee();

        System.out.println("\n Making tea....");
        tea.prepareRecipe();

        System.out.println("\n Making coffee....");
        coffee.prepareRecipe();
    }
}
