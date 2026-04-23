package uz.itpu.pt1.ex1;

public class SleepDemo {
    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();
    }
}