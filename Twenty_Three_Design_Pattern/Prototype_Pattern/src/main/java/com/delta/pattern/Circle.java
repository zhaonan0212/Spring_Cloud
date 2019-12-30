package com.delta.pattern;

import java.util.Scanner;

public class Circle implements Shape {
    @Override
    public Object clone() {
        Circle circle = null;
        try {
            circle = (Circle) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("克隆失败");
        }
        return circle;
    }

    @Override
    public void countArea() {
        int r = 0;
        System.out.println("这是一个圆,请输入半径");
        Scanner scanner = new Scanner(System.in);
        r = scanner.nextInt();
        System.out.println("面积="+3.14*r*r+"\n");
    }
}
