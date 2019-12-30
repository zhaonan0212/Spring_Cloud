package com.delta.Builder;

public class MacBook {
    private String mBoard;
    private String mDisplay;
    private String mOs;

    public MacBook() {
    }

    public MacBook(String mBoard, String mDisplay, String mOs) {
        this.mBoard = mBoard;
        this.mDisplay = mDisplay;
        this.mOs = mOs;
    }

    public String getmBoard() {
        return mBoard;
    }

    public void setmBoard(String mBoard) {
        this.mBoard = mBoard;
    }

    public String getmDisplay() {
        return mDisplay;
    }

    public void setmDisplay(String mDisplay) {
        this.mDisplay = mDisplay;
    }

    public String getmOs() {
        return mOs;
    }

    public void setmOs(String mOs) {
        this.mOs = mOs;
    }

    @Override
    public String toString() {
        return "MacBook{" +
                "mBoard='" + mBoard + '\'' +
                ", mDisplay='" + mDisplay + '\'' +
                ", mOs='" + mOs + '\'' +
                '}';
    }
}
