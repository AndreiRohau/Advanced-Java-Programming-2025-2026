package uz.itpu.pt_1.e2_queue;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.BlockingDeque;

/**
 * LinkedBlockingDeque (Two-Sided)
 * A "Deque" (Double-Ended Queue) allows you to insert or
 * remove items from both the front and the back.
 */
public class DequeExample {
    public static void main(String[] args) throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<>();

        deque.putFirst("First");
        deque.putLast("Last");

        System.out.println("Took from front: " + deque.takeFirst());
        System.out.println("Took from back: " + deque.takeLast());
    }
}

