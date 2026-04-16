package uz.itpu.pt_2.e3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrating BlockingQueue: The heart of Producer-Consumer logic.
 * Key Technical Aspects
 * Thread Coordination: It uses internal Conditions (part of ReentrantLock).
 *      When the queue is full, the producer calls notFull.await();
 *      when the consumer takes an item, it signals notFull.signal(), waking the producer up.
 * Capacity Bound:
 *      Bounded: (e.g., ArrayBlockingQueue) Has a fixed limit.
 *              Essential for preventing OutOfMemoryError by "back-pressuring" the producers.
 *      Unbounded: (e.g., LinkedBlockingQueue without a size) Can grow indefinitely,
 *              which is risky if producers are faster than consumers.
 * Null Prohibited: Like most concurrent collections,
 *      it does not allow null elements (it uses null as a return value for poll() to indicate the queue is empty).
 * Four Ways to Handle Operations:
 *      Throws Exception: add(e), remove(), element()
 *      Special Value (null/false): offer(e), poll(), peek()
 *      Blocks (Infinite wait): put(e), take()
 *      Times Out: offer(e, time, unit), poll(time, unit)
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialization
        // Capacity of 3: If 3 items are inside, the next producer will "block" (sit and wait).
        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(3);

        // 2. The Producer Thread
        Thread producer = new Thread(() -> {
            try {
                String[] items = {"Task 1", "Task 2", "Task 3", "Task 4"};
                for (String item : items) {
                    System.out.println("[Producer] Trying to add: " + item);

                    // put() is the key method: it blocks if the queue is full.
                    // When "Task 4" is reached, this thread will pause until the consumer takes one.
                    buffer.put(item);

                    System.out.println("[Producer] Successfully added: " + item);
                    Thread.sleep(500); // Simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 3. The Consumer Thread
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(2000); // Delay consumer to let the queue fill up
                while (true) {
                    // take() is the key method: it blocks if the queue is empty.
                    // It waits until a producer puts something in.
                    String item = buffer.take();

                    System.out.println("[Consumer] Processed: " + item);
                    if (item.equals("Task 4")) break; // Exit condition
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        // 4. Other useful methods (Non-blocking or Timed)

        // offer(): Tries to add, returns 'false' immediately if full (no waiting).
        boolean added = buffer.offer("Quick Task");

        // poll() with timeout: Waits for 1 second for an item to appear before giving up.
        String grabbed = buffer.poll(1, TimeUnit.SECONDS);

        System.out.println("Final check - Offer successful? " + added + " | Polled: " + grabbed);
    }
}

