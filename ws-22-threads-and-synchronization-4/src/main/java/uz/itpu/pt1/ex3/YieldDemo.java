package uz.itpu.pt1.ex3;

public class YieldDemo {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("start 1");
            Thread.yield();
            System.out.println("end 1");
        }).start();
        new Thread(() -> {
            System.out.println("start 2");
            System.out.println("end 2");
        }).start();
    }
}
