package com.delta.School;


import java.util.EventListener;

//抽象观察者类,铃声事件监听器
public interface BellEventListener extends EventListener {

     void heardBell(RingEvent event);
}
