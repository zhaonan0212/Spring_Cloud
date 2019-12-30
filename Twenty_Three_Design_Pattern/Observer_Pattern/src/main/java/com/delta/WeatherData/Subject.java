package com.delta.WeatherData;

/*
  author:zhaonan
  description:这是一个接口,主题的接口,管理观察者的注册和移除,通知的功能
 */
public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObserver();
}
