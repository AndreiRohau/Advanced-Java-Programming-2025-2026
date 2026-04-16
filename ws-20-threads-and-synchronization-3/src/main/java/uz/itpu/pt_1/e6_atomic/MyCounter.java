package uz.itpu.pt_1.e6_atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class MyCounter {
    public int count1;
    public volatile int count2;
    public AtomicInteger count3 = new AtomicInteger(0);
}
