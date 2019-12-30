package com.delta.Roles;

public class ConcreteConponent implements Component {

    public ConcreteConponent() {
        System.out.println("构建具体的角色");
    }

    @Override
    public void operation() {
        System.out.println("调用具体的方法构建角色");
    }
}
