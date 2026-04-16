package uz.itpu.pt_1.e3_synchronizer;

import java.util.concurrent.CyclicBarrier;

/**
 * meeting point.
 * A group of threads all wait for each other to reach a certain point before
 * they can all continue together.
 * Unlike a latch, it can be reused.
 */
public class BarrierExample {
    public static void main(String[] args) {
        // 3 friends must arrive before they start eating
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All arrived! Let's eat."));

        Runnable friend = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is driving...");
                barrier.await(); // Wait for others
                System.out.println(Thread.currentThread().getName() + " starts eating.");
            } catch (Exception e) { e.printStackTrace(); }
        };

        for (int i = 0; i < 3; i++) new Thread(friend).start();
    }
}

