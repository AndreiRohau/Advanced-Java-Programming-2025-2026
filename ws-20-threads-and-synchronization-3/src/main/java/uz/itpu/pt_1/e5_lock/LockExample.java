package uz.itpu.pt_1.e5_lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock (Basic Thread Locking)
 * This is a more flexible version of synchronized.
 * It allows you to check if a lock is available (tryLock) and
 * ensures fairness (giving the lock to the thread that waited the longest).
 */
public class LockExample {
    private final Lock lock = new ReentrantLock();

    public void safeMethod() {
        lock.lock(); // Manually acquire the lock
        try {
            System.out.println(Thread.currentThread().getName() + " is working...");
        } finally {
            lock.unlock(); // MUST unlock in finally block to avoid deadlocks
        }
    }

    public static void main(String[] args) {
        LockExample example = new LockExample();
        new Thread(example::safeMethod).start();
        new Thread(example::safeMethod).start();
    }
}

