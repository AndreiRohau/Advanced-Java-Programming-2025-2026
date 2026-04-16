package uz.itpu.pt_1.e3_synchronizer;

import java.util.concurrent.Semaphore;

/**
 * limited number of permits. It’s used to restrict how many threads can access a specific resource
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        // Only 2 threads can "acquire" a permit at a time
        Semaphore printerPool = new Semaphore(2);

        Runnable printTask = () -> {
            try {
                printerPool.acquire(); // Grab a permit
                System.out.println(Thread.currentThread().getName() + " is printing...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " done.");
                printerPool.release(); // Give the permit back
            }
        };

        for (int i = 1; i <= 4; i++) new Thread(printTask).start();
    }
}

