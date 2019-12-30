package com.delta.ThinkPad;

//这个需要在研究 一下
public class ThinkPad {
    private String mBoard;
    private String mDisplay;
    private String mOs;

    private ThinkPad(Builder builder){
        this.mBoard=builder.mBoard;
        this.mDisplay=builder.mDisplay;
        this.mOs=builder.mOs;
    }

    public String getBoard() {
        return mBoard;
    }

    public String getDisplay() {
        return mDisplay;
    }


    public String getOs() {
        return mOs;
    }

    public static class Builder{
        private String mBoard;
        private String mDisplay;
        private String mOs;

        public Builder setBoard(String board){
            this.mBoard=board;
            return this;
        }

        public Builder setDisplay(String display){
            this.mDisplay=display;
            return this;
        }

        public Builder setOs(String os){
            this.mOs=os;
            return this;
        }

        public ThinkPad builder(){
            return new ThinkPad(this);
        }
    }
}
