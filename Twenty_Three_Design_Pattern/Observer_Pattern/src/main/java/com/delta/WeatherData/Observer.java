package com.delta.WeatherData;

public interface Observer {
    public void update(float temperature,float pressure,float humidity);
}
