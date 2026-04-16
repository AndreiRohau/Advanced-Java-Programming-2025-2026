package uz.itpu.pt_1.e2_queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue (Bounded)
 * This queue has a fixed size.
 * If it's full, the Producer waits.
 * If it's empty, the Consumer waits.
 */
public class BlockingQueueExample {
    public static void main(String[] args) throws InterruptedException {
        // Capacity of only 2 items
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);

        // Producer Thread
        new Thread(() -> {
            try {
                queue.put(1); // Added = 1
                queue.put(2); // Added = 2
                System.out.println("Queue full. Producer waiting..."); // = 3
                queue.put(3); // This blocks until a consumer takes an item = 5
                System.out.println("Producer finally added 3!"); // = 6
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();

        Thread.sleep(2000); // Wait a bit before consuming

        // Consumer Thread
        new Thread(() -> {
            try {
                System.out.println("Consumer took: " + queue.take()); // = 4
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();
    }
}

