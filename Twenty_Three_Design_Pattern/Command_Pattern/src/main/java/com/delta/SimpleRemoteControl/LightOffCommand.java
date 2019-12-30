package com.delta.SimpleRemoteControl;


public class LightOffCommand implements Command {

    private Light light;
    private String preLuminance;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    @Override
    public void excute() {
        preLuminance = light.getLuminance();
        light.off();
    }

    @Override
    public void undo() {
        if (Light.HIGH.equals(preLuminance)) {
            light.high();
        } else if (Light.MEDIUM.equals(preLuminance)) {
            light.medium();
        } else if (Light.LOW.equals(preLuminance)) {
            light.low();
        } else if (Light.OFF.equals(preLuminance)) {
            light.off();
        }
    }
}
