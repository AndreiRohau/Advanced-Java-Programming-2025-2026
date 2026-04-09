package uz.itpu.pt_2.ex6;
//import java.util.concurrent.locks.Lock;


/**
 * The LockDemo class creates a counter and creates and runs 100 threads that change the value of the counter.
 * When the thread running is completed, the value of the counter is displayed in the output console.
 */
public class LockDemo {
    public static void main(String[] args) {
        MyCounter counter = new MyCounter();
        MyCounterThread[] threads = new MyCounterThread[100];

        for (int i = 0; i < 100; i++)
            threads[i] = new MyCounterThread(counter, 1_000_000);
        for (MyCounterThread thr: threads)
            thr.start();
        try {
            for (MyCounterThread thr: threads) {
                thr.join();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        System.out.println(counter.getValue());
    }
}
