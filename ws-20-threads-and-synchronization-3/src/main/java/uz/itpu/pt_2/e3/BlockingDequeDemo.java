package uz.itpu.pt_2.e3;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrating BlockingDeque: Operations at both ends.
 * Key Technical Aspects
 *  Dual-End Blocking: Every "put" and "take" operation has a First and Last variant.
 *          This gives you total control over task priority (e.g., jumping to the front of the line).
 *  Work-Stealing Foundation: While BlockingDeque provides the structure,
 *          it serves as the conceptual basis for ForkJoinPool,
 *          where idle threads "steal" work from the back of busy threads' deques to stay productive.
 *  LIFO vs FIFO:
 *      FIFO (Queue): Use putLast() and takeFirst().
 *      LIFO (Stack): Use putFirst() and takeFirst() (or push() and pop()).
 *  Thread Coordination: Like BlockingQueue, it uses a single lock (or dual locks in some custom implementations) and
 *          Condition objects to signal when the deque is no longer full or no longer empty.
 */
public class BlockingDequeDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialization
        // Can be bounded (fixed size) or unbounded.
        BlockingDeque<String> deque = new LinkedBlockingDeque<>(3);

        // 2. Producer - Adding to both ends
        Thread producer = new Thread(() -> {
            try {
                // putFirst(): Blocks if full. Adds to the head.
                deque.putFirst("Urgent Task");
                System.out.println("[Producer] Added to Front: Urgent Task");

                // putLast(): Blocks if full. Adds to the tail (standard Queue behavior).
                deque.putLast("Normal Task");
                System.out.println("[Producer] Added to Back: Normal Task");

                // Adding one more to fill capacity
                deque.putLast("Late Task");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 3. Consumer - Taking from both ends
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(1000); // Wait for producer to fill deque

                // takeFirst(): Blocks if empty. Removes from the head.
                System.out.println("[Consumer] Processed from Front: " + deque.takeFirst());

                // takeLast(): Blocks if empty. Removes from the tail.
                // This effectively treats the deque like a Stack (LIFO).
                System.out.println("[Consumer] Processed from Back: " + deque.takeLast());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 4. Stack-like Methods
        // push() and pop() are shorthand for addFirst() and removeFirst().
        deque.push("Stack Item");
        System.out.println("Popped: " + deque.pop());

        // 5. Timed Blocking
        // pollLast() waits for a specific time for an item to appear at the tail.
        String result = deque.pollLast(500, TimeUnit.MILLISECONDS);
        System.out.println("Timed Poll Last result: " + result);
    }
}
