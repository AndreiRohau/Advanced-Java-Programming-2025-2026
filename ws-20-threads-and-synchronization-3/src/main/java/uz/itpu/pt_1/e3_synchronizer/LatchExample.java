package uz.itpu.pt_1.e3_synchronizer;

import java.util.concurrent.CountDownLatch;

/**
 * countdown.
 * One or more threads wait until a set of operations being performed in other threads completes.
 * It cannot be reset.
 */
public class LatchExample {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3); // Wait for 3 services

        Runnable service1 = () -> {
            System.out.println("Service 1 started...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown(); // Decrement count
        };

        Runnable service2 = () -> {
            System.out.println("Service 2 started...");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown(); // Decrement count
        };

        Runnable service3 = () -> {
            System.out.println("Service 3 started...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown(); // Decrement count
        };

        new Thread(service1).start();
        new Thread(service2).start();
        new Thread(service3).start();

        latch.await(); // Main thread waits until count is 0
        System.out.println("All services up! System launching.");
    }
}
