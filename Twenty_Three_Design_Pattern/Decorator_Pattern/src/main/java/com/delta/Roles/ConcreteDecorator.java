package com.delta.Roles;

public class ConcreteDecorator extends  Decorator {



    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        addFunction();
    }


    public  void addFunction(){
        System.out.println("为具体狗i见角色添加额外的功能addFunction()");
    }
}
