package com.delta.ThinkPad;

public class ThinkPadTest {
    public static void main(String[] args) {
        ThinkPad thinkPad = new ThinkPad.Builder().setBoard("Intel").setDisplay("三星显示器").setOs("Windows 10").builder();
        System.out.println(thinkPad.getBoard());
        System.out.println(thinkPad.getDisplay());
        System.out.println(thinkPad.getOs());
    }
}
