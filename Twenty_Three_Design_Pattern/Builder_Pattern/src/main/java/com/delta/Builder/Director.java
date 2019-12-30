package com.delta.Builder;

//指挥者类
public class Director {

    Builder mBuilder = null;

    public Director(Builder builder) {
        this.mBuilder = builder;
    }

    public void constract (String board,String display,String os){
        mBuilder.buildBoard(board);
        mBuilder.buildDisplay(display);
        mBuilder.buildOs(os);

    }
    /*
    生活中有很多例子,如游戏的不同角色,其性别,个性,武器,脸型,体重,等都有所差异,还有汽车的方向盘,发动机,车架,轮胎等,这些产品都是由许多部件组成,每个部件灵活选择,
    但其创建的步骤大同小异,就使用建造者模式
     */


    /*
    缺点:
      1.产品的组成必须相同,这限制了使用的范围
      2.如果产品内部变化复杂,就需要增加很多建造者类
     */

    //建造者模式注重的是零件的组装过程,工厂模式关注的是零件的创建过程
}
