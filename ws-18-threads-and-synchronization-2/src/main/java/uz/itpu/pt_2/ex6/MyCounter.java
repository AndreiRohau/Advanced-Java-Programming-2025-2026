package uz.itpu.pt_2.ex6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The MyCounter class describes a counter that will be used as a common resource for threads.
 */
public class MyCounter {
    private long count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    public long getValue() {
        return count;
    }
}
