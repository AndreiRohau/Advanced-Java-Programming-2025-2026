package uz.itpu.pt_1.e6_atomic;

public class MyCountThread extends Thread {
    MyCounter meter;
    int number;

    public MyCountThread(MyCounter counter, int number) {
        this.meter = counter;
        this.number = number;
    }

    public void run() {
        for (int i = 0; i < number; i++) {
            this.meter.count1++;
            this.meter.count2++;
            this.meter.count3.getAndIncrement();
        }
    }
}
