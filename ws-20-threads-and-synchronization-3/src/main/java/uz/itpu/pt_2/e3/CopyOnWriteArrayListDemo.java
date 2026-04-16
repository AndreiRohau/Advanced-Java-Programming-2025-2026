package uz.itpu.pt_2.e3;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demonstrating CopyOnWriteArrayList (COW) behavior.
 * Key Technical Aspects
 * The "Write" Process: When you call .add(), the class locks a reentrant lock,
 *      copies the current array to a new one (size + 1), adds the element,
 *      and then points the internal reference to the new array.
 * Zero Locking for Reads: Because the underlying array is never modified in place,
 *      readers don't need to worry about seeing a "half-added" element.
 *      They always see a consistent, completed version of the array.
 * Fail-Safe Iterators: Iterators do not support remove(). If you try it.remove(),
 *      it throws UnsupportedOperationException because the iterator is traversing a static snapshot, not the live list.
 * Memory Overhead: It is not suitable for large lists with frequent writes,
 *      as every write duplicates the entire array, which can lead to high memory consumption and GC pressure.
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialization
        // Internally, it holds a volatile array: private transient volatile Object[] array;
        CopyOnWriteArrayList<String> sensors = new CopyOnWriteArrayList<>();
        sensors.add("Temperature");
        sensors.add("Humidity");
        sensors.add("Pressure");

        // 2. Snapshot Iteration (The "Magic")
        // The iterator works on a 'snapshot' of the array from the moment the iterator was created.
        Iterator<String> it = sensors.iterator();

        // 3. Concurrent Modification
        // We modify the list AFTER the iterator is already created.
        sensors.add("CO2");
        // This 'add' creates a brand new array. The iterator 'it' is still looking at the old array.

        System.out.println("--- Snapshot View (Old Array) ---");
        while (it.hasNext()) {
            // This will NOT show "CO2" and will NOT throw ConcurrentModificationException.
            System.out.println("Iterator element: " + it.next());
        }

        System.out.println("--- Current View (New Array) ---");
        // Fresh reads see the new array immediately due to 'volatile' visibility.
        sensors.forEach(s -> System.out.println("Live element: " + s));

        // 4. Multi-threaded "Read-Heavy" Simulation
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Many threads can read simultaneously without any locks or waiting
        Runnable reader = () -> {
            String val = sensors.get(0);
            System.out.println(Thread.currentThread().getName() + " read: " + val);
        };

        // Writing is expensive: involves ReentrantLock + Arrays.copyOf()
        Runnable writer = () -> {
            sensors.addIfAbsent("Light"); // Atomic add-if-not-present
            System.out.println(Thread.currentThread().getName() + " added Light");
        };

        executor.execute(reader);
        executor.execute(writer);
        executor.execute(reader);

        executor.shutdown();
    }
}

