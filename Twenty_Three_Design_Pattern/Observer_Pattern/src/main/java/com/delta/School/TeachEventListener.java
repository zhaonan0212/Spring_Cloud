package com.delta.School;

public class TeachEventListener  implements BellEventListener{
    @Override
    public void heardBell(RingEvent event) {
        if (event.getSound()){
            System.out.println("老师上课啦...");
        }else{
            System.out.println("老师下课啦...");
        }
    }
}
