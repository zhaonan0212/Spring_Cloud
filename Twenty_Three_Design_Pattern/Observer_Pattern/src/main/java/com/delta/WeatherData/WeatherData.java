package com.delta.WeatherData;

import java.util.ArrayList;

/*
  author:
  description:这个就是主题,主题一变动,发通知给其他观察者
 */
public class WeatherData implements Subject {

    private ArrayList observers;
    private float temperature;
    private float pressure;
    private float humidity;

    public WeatherData() {
        observers = new ArrayList();
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0){
            observers.remove(i);
        }
    }

    @Override
    public void notifyObserver() {
        for (int i = 0 ; i < observers.size();i++){
            Observer observer = (Observer) observers.get(i);
            observer.update(temperature,pressure,humidity);
        }
    }

    //添加一个修改参数的方法
    public void measurementChanged(){
        notifyObserver();
    }

    public void setMeasurement(float temperature,float pressure,float humidity){
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = temperature;
        measurementChanged();
    }

}
