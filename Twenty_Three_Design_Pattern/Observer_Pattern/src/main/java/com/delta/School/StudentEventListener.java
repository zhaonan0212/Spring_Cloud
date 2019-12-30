package com.delta.School;

public class StudentEventListener implements BellEventListener {
    @Override
    public void heardBell(RingEvent event) {
        if (event.getSound()){
            System.out.println("同学们,上课啦...");
        }else {
            System.out.println("同学们,下课啦....");
        }
    }
}
