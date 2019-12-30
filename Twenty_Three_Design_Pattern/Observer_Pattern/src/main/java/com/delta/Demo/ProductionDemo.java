package com.delta.Demo;


import java.util.ArrayList;

public class ProductionDemo implements SubjectDemo {

    private String name;
    private String password;
    private int age;
    //这里为什么要有一个observer的集合
    private ArrayList observers;

    public ProductionDemo() {
        observers = new ArrayList();
    }

    public ProductionDemo(String name, String password, int age) {
        this.name = name;
        this.password = password;
        this.age = age;
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
    public void registerObserver(ObserverDemo o) {
        observers.add(o);
    }

    @Override
    public void notifyObserver() {
        for (int i= 0;i<observers.size();i++){
            ObserverDemo observer = (ObserverDemo) observers.get(i);
            observer.update(name,password,age);
        }
    }


    public void setMeasurements(String name,String password,int age){
        this.age=age;
        this.name =name;
        this.password=password;
        measurementsChange();
    }

    public void measurementsChange(){
        notifyObserver();
    }

    @Override
    public String toString() {
        return "ProductionDemo{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
