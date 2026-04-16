package uz.itpu.pt_2.ex1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The queue is initially empty. An attempt to add three elements is made in the first thread.
 * Two elements will be added successfully, and if you try to add a third, the thread will be suspended
 * until there is free space in the queue. Only when the second thread retrieves one element and
 * frees up space will the first thread get a chance to add the third element.
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(2);
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    queue.put("Java" + i);
                    System.out.println("Element " + i + " added");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            try {
                System.out.println("Element " + queue.take() + " took");
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
