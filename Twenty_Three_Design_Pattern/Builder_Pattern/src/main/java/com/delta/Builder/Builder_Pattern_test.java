package com.delta.Builder;

public class Builder_Pattern_test {
    public static void main(String[] args) {
        Builder builder = new MacBookBuilder();
        Director director = new Director(builder);

        director.constract("因特尔主板","Retina显示器","Mac Ox 操作系统");
        System.out.println(builder.getMacBook().toString());

    }
}
