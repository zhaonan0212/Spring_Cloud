package com.delta.School;

import java.util.EventObject;

/*
  author:
  description:按铃事件类
 */
public class RingEvent extends EventObject {
    private static final long serialVersionUID = 1l;
    private boolean sound;                   //true表示上课,false表示下课

    public RingEvent(Object source,Boolean sound) {
        super(source);
        this.sound =sound;
    }

    public boolean getSound() {
        return this.sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
}
