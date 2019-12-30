package com.delta.SimpleRemoteControl;

public class RemoteControl {

    private Command off;
    private Command high;
    private Command medium;
    private Command low;
    private Command preCommand;

    public void setOff(Command off) {
        this.off = off;

    }

    public void setHigh(Command high) {
        this.high = high;
    }

    public void setMedium(Command medium) {
        this.medium = medium;
    }

    public void setLow(Command low) {
        this.low = low;

    }

    public void lightOff() {
        off.excute();
        preCommand = off;
    }

    public void lightHigh() {
        high.excute();
        preCommand = high;

    }

    public void lightMedium() {
        medium.excute();
        preCommand = medium;

    }

    public void lightLow() {
        low.excute();
        preCommand = low;
    }

    public void undo() {
        if (preCommand == null) {
            System.out.println("无法撤销");
        } else {
            preCommand.undo();
        }
    }
}
