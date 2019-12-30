package com.delta.Motor;


public class ElectricAdapter implements Motor {

    private ElectricMotor emotor;

    public ElectricAdapter() {
        emotor = new ElectricMotor();
    }

    @Override
    public void drive() {
        emotor.electricDrive();
    }
}
