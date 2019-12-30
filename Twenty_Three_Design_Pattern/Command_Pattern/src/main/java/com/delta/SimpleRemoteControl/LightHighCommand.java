package com.delta.SimpleRemoteControl;

public class LightHighCommand implements Command {

    private  Light light;
    private  String preLuminance;

    @Override
    public void excute() {
        preLuminance = light.getLuminance();
        light.high();
    }

    @Override
    public void undo() {
        if (Light.HIGH.equals(preLuminance)){
            light.high();
        }else if(Light.MEDIUM.equals(preLuminance) ){
            light.medium();
        }else if (Light.LOW.equals(preLuminance)){
            light.low();
        }else if (Light.OFF.equals(preLuminance)){
            light.off();
        }
    }

    public LightHighCommand(Light light) {
        this.light = light;
    }

    public void setLight(Light light) {
        this.light = light;
    }
}
