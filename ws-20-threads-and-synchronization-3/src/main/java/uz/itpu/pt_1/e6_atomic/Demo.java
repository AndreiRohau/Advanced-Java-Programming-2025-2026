package uz.itpu.pt_1.e6_atomic;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        MyCounter counter = new MyCounter();

        int threadCount = 100;
        int incrementCount = 1_000_000;

        MyCountThread[] threads = new MyCountThread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new MyCountThread(counter, incrementCount);
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        System.out.println("Regular field (count1): " + counter.count1);
        System.out.println("Volatile field (count2): " + counter.count2);
        System.out.println("Atomic field (count3): " + counter.count3.get());
    }
}

