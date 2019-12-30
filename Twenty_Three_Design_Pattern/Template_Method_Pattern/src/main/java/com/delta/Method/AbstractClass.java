package com.delta.Method;

/*
  1.例子：一个人每天起床，吃饭，睡觉，做事等，其中”做事“的内容每天可能都不同，我们把这些规定了流程或格式的实例定义成模板，
  2.模板方法：1.它封装了不变的代码，扩展可变的部分，他把认为不变部分的算法封装到父类中实现，可变部分由子类继承实现，便于子类扩展
          2.它在父类中提取的公共的代码，便于复用
          3.可变部分子类实现，
        缺点：每个不同部分的实现都需要定义一个子类，这会导致类的个数很多，系统很庞大
             父类中抽象方法由子类实现，子类执行的结果会影响父类的结果
  3.模板方法模式的结构：
        1.抽象类：负责给出一个算法的轮廓和骨架，由一个模板和若干个基本方法构成
           模板方法：定义了算法的骨架，按某种顺序调用其包含的基本方法。

           基本方法：是整个算法中的一个步骤，包含以下几种类型。
           抽象方法：在抽象类中申明，由具体子类实现。
           具体方法：在抽象类中已经实现，在具体子类中可以继承或重写它。
           钩子方法：在抽象类中已经实现，包括用于判断的逻辑方法和需要子类重写的空方法两种
        2.具体子类：实现抽象类中的抽象方法和钩子，
 */
public abstract class AbstractClass {
    public  void TemplateMethod(){
        SpecificMethod();
        abstractMethod1();
        abstractMethod2();
    }

    private void SpecificMethod() {
        System.out.println("抽象类中的具体方法被调用");
    }

    public abstract void abstractMethod1();

    public abstract void abstractMethod2();

}
