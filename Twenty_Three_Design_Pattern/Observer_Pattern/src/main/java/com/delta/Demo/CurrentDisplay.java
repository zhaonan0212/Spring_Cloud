package com.delta.Demo;

public class CurrentDisplay implements DisplayDemo,ObserverDemo {
    private String name;
    private String password;
    private int age;
    private SubjectDemo subjectDemo;

    public CurrentDisplay(SubjectDemo subjectDemo) {
        this.subjectDemo = subjectDemo;

        //这里还要注入
        subjectDemo.registerObserver(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void display() {
        System.out.println("姓名:"+name +"面膜:"+password+"年龄:"+age);
    }

    @Override
    public void update(String name, String password, int age) {
        this.name = name;
        this.password = password;
        this.age = age;
        display();
    }
}
