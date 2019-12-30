package com.delta.SimpleRemoteControl;

public class Light {
    public static final String HIGH = "贼亮";
    public static final String MEDIUM = "有点亮";
    public static final String LOW = "快灭火了";
    public static final String OFF = "真的灭火了~";
    private String luminance;

    public Light() {
        this.luminance = OFF;
    }

    public void off(){
        System.out.println("灯关闭了");
        this.luminance =OFF;
    }

    public void high() {
        System.out.println("贼亮");
        this.luminance = HIGH;
    }

    public void medium() {
        System.out.println("挺亮地！");
        this.luminance = MEDIUM;
    }

    public void low() {
        System.out.println("快灭火了");
        this.luminance = LOW;
    }

    public String getLuminance(){
        return this.luminance;
    }

}
