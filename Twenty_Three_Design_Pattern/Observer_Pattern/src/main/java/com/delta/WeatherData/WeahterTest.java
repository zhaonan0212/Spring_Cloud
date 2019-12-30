package com.delta.WeatherData;

public class WeahterTest {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        weatherData.setMeasurement(80,65,20.5f);
        CurrentConditionDisplay currentConditionDisplay = new CurrentConditionDisplay(weatherData);
        weatherData.setMeasurement(80,66,23.5f);
        CurrentConditionDisplay currentConditionDisplay1 = new CurrentConditionDisplay(weatherData);
        weatherData.setMeasurement(87,55,20.5f);
        CurrentConditionDisplay currentConditionDisplay2 = new CurrentConditionDisplay(weatherData);
    }
}
