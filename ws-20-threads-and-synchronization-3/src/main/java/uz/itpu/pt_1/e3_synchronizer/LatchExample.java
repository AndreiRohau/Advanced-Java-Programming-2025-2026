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

        Runnable service = () -> {
            System.out.println("Service started...");
            latch.countDown(); // Decrement count
        };

        new Thread(service).start();
        new Thread(service).start();
        new Thread(service).start();

        latch.await(); // Main thread waits until count is 0
        System.out.println("All services up! System launching.");
    }
}
