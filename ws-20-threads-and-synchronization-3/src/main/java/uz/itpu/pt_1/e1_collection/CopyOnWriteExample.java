package uz.itpu.pt_1.e1_collection;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * Threads read from the current array.
 * Since the array itself never changes (it is "effectively immutable"),
 * multiple threads can read it at the exact same time without any risk of errors.
 *
 * When you try to add() or remove() something, the list doesn't change the existing array. Instead:
 * Step A: It makes a complete copy of the entire array.
 * Step B: It performs the change (like adding a new item) on that new copy.
 * Step C: It then "swaps" the old array with the new one.
 *
 * If a thread is halfway through a loop when a write happens, it continues on the old version of the array.
 * How it works: Because the writer created a new copy,
 * the old array still exists for the readers who were already using it.
 * Result: You will never get a ConcurrentModificationException.
 */
public class CopyOnWriteExample {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
//        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");

        // todo Safe to iterate while another process might add to the list
        for (String item : list) {
            System.out.println("Reading: " + item);
            list.add("Cherry"); // todo This won't affect the current loop
        }

        System.out.println("Final List: " + list);
    }
}

