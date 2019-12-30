package com.delta.Hook;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Coffee extends CaffeineBeverageHook {
    @Override
    public void brewCoffeeGrinds() {
        System.out.println("Dripping Coffee through filter");
    }

    @Override
    public void addSugarAndMilk() {
        System.out.println("Adding sugar and milk");
    }


    public boolean customerWantsCondiments(){
        String answer = getResult();
        if (answer.toUpperCase().startsWith("Y")){
            return true;
        }else{
            return  false;
        }
    }

    private String getResult() {
        String answer = null;
        System.out.println("would you like sugar and milk (y/n)?");

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        answer = null;
        try {
            answer = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (answer == null){
            return "No";
        }
        return  answer;
    }

}
