package uz.itpu.pt_1.e1_collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Reading is never blocked, since the data is marked volatile.
 * Mutating syncs only bucket for the mutation, instead of full block
 */
public class ConcurrentMapExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);

        // todo Multiple threads can safely iterate and modify at the same time
        map.forEach((key, value) -> {
            System.out.println(key + ": " + value);
            if (key.equals("Java")) {
                map.put("C++", 3); // todo Safe to modify during iteration
            }
        });

        System.out.println("Final Map: " + map);
    }
}

