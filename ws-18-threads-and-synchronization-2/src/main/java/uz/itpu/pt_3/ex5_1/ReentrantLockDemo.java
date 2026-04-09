package uz.itpu.pt_3.ex5_1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    Lock lock = new ReentrantLock();

    int count = 0;

    public void increment() {
        lock.lock(); // Only 1 thread can get here at a time
        try {
            count++;
        } finally {
            lock.unlock(); // Always release in finally!
        }
    }

}
