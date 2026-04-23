package uz.itpu.pt1.ex6;

import java.util.concurrent.TimeUnit;

public class ThreadExceptionDemo {
    public static void main(String[] args) {
        new Thread(() -> {
            if (Boolean.TRUE) {
                throw new RuntimeException();
            }
            System.out.println("end of Thread");
        }).start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        System.out.println("end of main thread");
    }
}
