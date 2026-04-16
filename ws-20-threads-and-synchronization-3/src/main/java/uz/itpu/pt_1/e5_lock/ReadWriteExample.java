package uz.itpu.pt_1.e5_lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock (Read/Write Separation)
 * This is perfect when you have many readers but few writers.
 * It allows any number of threads to read simultaneously as long as no one is writing.
 */
public class ReadWriteExample {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private String data = "Initial Data";

    public void read() {
        rwLock.readLock().lock(); // Multiple threads can hold this lock
        try {
            System.out.println("Read: " + data);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void write(String newValue) {
        rwLock.writeLock().lock(); // Only one thread can hold this; blocks all readers
        try {
            data = newValue;
            System.out.println("Data updated!");
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

