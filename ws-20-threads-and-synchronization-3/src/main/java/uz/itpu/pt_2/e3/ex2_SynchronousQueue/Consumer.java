package uz.itpu.pt_2.e3.ex2_SynchronousQueue;

import java.util.concurrent.SynchronousQueue;

/**
 * The thread-consumer consumes data and also gets a reference to the SynchronousQueue.
 * It then calls the take() method in a loop until it receives the string "DONE" and completes the work.
 * Data will only be passed when both threads are ready.
 */
public class Consumer implements Runnable {
    private SynchronousQueue<String> drop;
    public Consumer(SynchronousQueue<String> drop) {
        this.drop = drop;
    }
    public void run() {
        try {
            String msg = null;
            while (!((msg = drop.take()).equals("DONE"))) {
                System.out.println(msg);
            }
        } catch(InterruptedException intEx) {
            System.out.println("Interrupted! Last one out, turn out the lights!");
        }
    }
}
