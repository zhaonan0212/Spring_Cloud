package com.delta;

public class Demo02 implements Runnable{

    private int number;

    public Demo02(int number) {
        this.number = number;
    }

    public static void main(String[] args) {

        int[] numbers = new int[]{10,15,5,20,11};

        for (int number : numbers) {
            new Thread(new Demo02(number)).start();
        }

    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println(number);
        } catch (InterruptedException e) {


        }
    }
}
