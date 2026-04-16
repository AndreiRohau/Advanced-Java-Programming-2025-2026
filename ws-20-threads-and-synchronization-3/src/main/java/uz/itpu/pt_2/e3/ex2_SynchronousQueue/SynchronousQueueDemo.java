package uz.itpu.pt_2.e3.ex2_SynchronousQueue;

import java.util.concurrent.SynchronousQueue;

/**
 * The SynchronousQueueDemo class is the main thread that creates a queue of the SynchronousQueue type and
 * also launches the consumer and producer threads for execution.
 */
public class SynchronousQueueDemo {
    public static void main(String[] args) {
        SynchronousQueue<String> drop = new SynchronousQueue<String>();
        new Thread( new Producer(drop)).start();
        new Thread( new Consumer(drop)).start();
    }
}
