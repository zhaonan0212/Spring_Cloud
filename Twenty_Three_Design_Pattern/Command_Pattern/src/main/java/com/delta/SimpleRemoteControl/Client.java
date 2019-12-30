package com.delta.SimpleRemoteControl;

/*
  这是一个带撤回功能的命令模式
 */
public class Client {
    public static void main(String[] args) {
        // 创建接收者
        Light light = new Light();
        // 创建命令
        Command lightHighCommand = new LightHighCommand(light);
        Command lightOffCommand = new LightOffCommand(light);
        // 创建调用者
        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setHigh(lightHighCommand);
        remoteControl.setOff(lightOffCommand);
        // 调用
        remoteControl.lightHigh();
        remoteControl.lightOff();
        remoteControl.undo();
    }
}
