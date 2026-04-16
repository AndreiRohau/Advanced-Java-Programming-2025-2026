package uz.itpu.pt_1.e4_executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Instead of manually creating new Thread() every time,
 * you use an ExecutorService.
 * This manages a "pool" of threads that sit ready to work,
 * saving the high cost of creating and destroying threads constantly.
 */
public class ThreadPoolExample {
    public static void main(String[] args) {
        // Create a pool with 3 fixed threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Running task in: " + Thread.currentThread().getName());
                }
            });
        }

        executor.shutdown(); // Always shut down to stop the threads
    }
}

