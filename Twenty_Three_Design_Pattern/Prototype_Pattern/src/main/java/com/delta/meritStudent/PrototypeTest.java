package com.delta.meritStudent;

/*
原型模式的使用场景:
  对象间相同或相似,即只有个别属性不同的时候
  对象的创建比较麻烦,但是复制比较简单
 */
public class PrototypeTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        Citation citation = new Citation("小红","同学,在2019年表现优异,评为三好学生","韶关学院");
        citation.display();

        Citation citation1 =(Citation)citation.clone();
        citation1.setName("小南");
        citation1.display();
    }
}
