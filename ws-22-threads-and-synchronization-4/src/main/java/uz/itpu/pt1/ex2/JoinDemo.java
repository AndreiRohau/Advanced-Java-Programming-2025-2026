package uz.itpu.pt1.ex2;

import java.util.concurrent.TimeUnit;

public class JoinDemo {
    public static void main(String[] args) {
        System.out.println("start of " + Thread.currentThread().getName());
        new Thread(() -> {
            System.out.println("start 1");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end 1");
        }).start();
        JoinThread thread = new JoinThread();
        thread.start();
        try {
            thread.join(100);
            // or TimeUnit.MILLISECONDS.timedJoin(thread, 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end of " + Thread.currentThread().getName());
    }
}
