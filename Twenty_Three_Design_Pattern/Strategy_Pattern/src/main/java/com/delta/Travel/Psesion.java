package com.delta.Travel;

public class Psesion {
    private Travel travel;

    public Psesion(Travel travel) {
        this.travel = travel;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public void goOut(){
        travel.goOut();
    }


}
