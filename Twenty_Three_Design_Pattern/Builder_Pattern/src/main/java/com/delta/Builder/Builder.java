package com.delta.Builder;

public abstract class Builder {
    //设置主题
    public abstract void buildBoard(String board);
    //显示器
    public abstract void buildDisplay(String display);
    //操作系统
    public abstract void buildOs(String os);


    //创建computer
    public abstract MacBook getMacBook();


}
