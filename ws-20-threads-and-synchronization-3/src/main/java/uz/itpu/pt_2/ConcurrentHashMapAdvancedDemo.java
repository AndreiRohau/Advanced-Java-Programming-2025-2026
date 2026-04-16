package uz.itpu.pt_2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrating ConcurrentHashMap (CHM) features and thread-safety.
 * Key Technical Aspects
 * No Global Lock: While Hashtable locks the whole map for every operation,
 *      ConcurrentHashMap only locks the specific bucket being updated.
 * Lock-Free Reads: The get() operation typically doesn't use locks at all,
 *      relying on volatile memory visibility to see the latest updates.
 * Weak Consistency: Iterators reflect the state of the map at the time they were created and
 *      do not throw exceptions if another thread modifies the map during iteration.
 * Scalability: By default, it supports a concurrency level of 16, meaning up to 16 threads can perform
 *      write operations simultaneously if they hit different buckets.
 */
public class ConcurrentHashMapAdvancedDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialization
        // CHM does NOT allow null keys or values. Throws NullPointerException if attempted.
        ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();

        // 2. Atomic "Check-then-Act" Operations
        // These methods are atomic, meaning no other thread can intervene between the check and the update.

        // putIfAbsent: Only adds if the key isn't already there.
        stock.putIfAbsent("Laptop", 10);

        // computeIfPresent: Safely updates an existing value based on current data.
        stock.computeIfPresent("Laptop", (key, val) -> val + 5);

        // computeIfAbsent: Useful for lazy initialization (e.g., setting a default if missing).
        stock.computeIfAbsent("Phone", key -> 20);

        // merge: Combines existing value with new data using a function.
        // If "Laptop" exists, add 2 to it; otherwise, set it to 2.
        stock.merge("Laptop", 2, Integer::sum);

        System.out.println("Initial Stock: " + stock);

        // 3. Multi-threaded Access Simulation
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Thread 1: Rapidly updates the same key
        executor.submit(() -> {
            for (int i = 0; i < 50; i++) {
                // compute() ensures the increment is thread-safe and atomic per key.
                stock.compute("Laptop", (k, v) -> v + 1);
            }
        });

        // Thread 2: Reads and adds new keys simultaneously
        executor.submit(() -> {
            stock.put("Tablet", 15);
            // Weakly Consistent Iterators: Unlike HashMap, CHM won't throw
            // ConcurrentModificationException if the map changes during this loop.
            stock.forEach((k, v) -> System.out.println("Concurrent Read -> " + k + ": " + v));
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // 4. Advanced Parallel Operations (Java 8+)
        // Performs parallel reduction across the map if the size exceeds the threshold.
        long threshold = 1;
        int totalItems = stock.reduceValues(threshold, Integer::sum);

        System.out.println("Final Stock Map: " + stock);
        System.out.println("Total Inventory Count: " + totalItems);
    }
}

