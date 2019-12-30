package com.delta.Motor;

/*
      电能适配器
 */
public class OpticalAdapter implements Motor {

    private OpticalMotor omotor;

    public OpticalAdapter() {
        omotor = new OpticalMotor();
    }

    @Override
    public void drive() {
        omotor.opticalMotor();
    }
}
