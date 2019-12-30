package com.delta.School;

/*
观察者模式使用的场景:
 1.对象间存在一对多的关系,一个对象的状态发生改变会影响其他对象
 2.当一个抽象模型有两个方面,其中一个方面依赖另一个方面时,可将这二者封装在独立的对象中以使他们可以各自独立的改变和使用
 */
public class BellEvenTest {
    public static void main(String[] args) {
        BellEventSource bell = new BellEventSource();   //铃声
        bell.addPersonListener(new TeachEventListener());   //注册监听器,老师
        bell.addPersonListener(new StudentEventListener()); //注册学生

        bell.ring(true);  //上课铃铃
        System.out.println("================");
        bell.ring(false);  //下课铃
    }
}
