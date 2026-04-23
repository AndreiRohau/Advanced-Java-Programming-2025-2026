package uz.itpu.pt2.ex1;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static final int ITEMS_COUNT = 10;
    public final static Semaphore sortSemaphore = new Semaphore(0, true);
    public static void main(String[] args) {
//        int[] items = new int[ITEMS_COUNT];
//        for (int i = 0; i < items.length; i++) {
//            items[i] = (int) (Math.random()*10);
//        }
        int[] items = {8, 9, 6, 7, 9, 2, 4, 8, 6, 3};
        System.out.println("Initial array: " + Arrays.toString(items));
        new Thread(new ArraySort(items)).start();
        for (int i = 0; i < items.length; i++) {
            sortSemaphore.acquireUninterruptibly();
            System.out.println("Step [" + (i + 1) + "]: " + Arrays.toString(items));
        }
        System.out.println("Sorted array: " + Arrays.toString(items));
    }
}
