package uz.itpu.pt_2.ex2_SynchronousQueue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * The thread-producer produces data and gets a reference to the SynchronousQueue.
 * It calls the put() method in a loop to insert data into the queue.
 * When all the data has been passed, the string "DONE" is passed to the queue as a sign of completion.
 */
public class Producer implements Runnable {
    private SynchronousQueue<String> drop;
    List<String> messages = Arrays.asList( "Mares eat oats", "Does eat oats",
            "Little lambs eat ivy",
            "Wouldn't you eat ivy too?");
    public Producer(SynchronousQueue<String> drop) {
        this.drop = drop;
    }
    public void run() {
        try {
            for (String str : messages) {
                drop.put(str);
            }
            drop.put("DONE");
        } catch(InterruptedException intEx) {
            System.out.println("Interrupted! Last one out, turn out the lights!");
        }
    }
}
