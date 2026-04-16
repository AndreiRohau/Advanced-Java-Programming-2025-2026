package uz.itpu.pt_3.ex5_1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Objects.isNull;

public class ReentrantReadWriteLockDemo {

    Map<String, String> cache = new HashMap<>();

    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String getValue(String key) {
        rwLock.readLock().lock(); // Multiple threads can read simultaneously
        try {
            return cache.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void putValue(String key, String value) {
        rwLock.writeLock().lock(); // Waits for all readers to finish, then a single writer enters
        try {
            cache.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

}
