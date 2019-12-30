package com.delta.pattern;

import java.util.Scanner;

public class Square implements Shape {
    @Override
    public Object clone() {
        Square square = null;
        try {
            square = (Square) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("克隆失败");
        }
    return square;
    }

    @Override
    public void countArea() {
        int r = 0;
        System.out.println("这是一个正方形,请输入边长");
        Scanner scanner = new Scanner(System.in);
        r = scanner.nextInt();
        System.out.println("面积="+r*r+"\n");
    }
}
