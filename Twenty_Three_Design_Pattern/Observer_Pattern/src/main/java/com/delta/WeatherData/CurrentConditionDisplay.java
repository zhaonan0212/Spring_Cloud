package com.delta.WeatherData;

public class CurrentConditionDisplay implements DisplayElement,Observer {
    private float temperature;
    private float pressure;
    private float humidity;
    private Subject weatherData;

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

    public CurrentConditionDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void display() {
        System.out.println("温度:"+temperature+"\n"+"湿度:"+humidity+"\n"+"压力:"+pressure+"\n");
    }


    @Override
    public void update(float temperature, float pressure, float humidity) {
        this.humidity=humidity;
        this.pressure=pressure;
        this.temperature = temperature;
        display();
    }
}
