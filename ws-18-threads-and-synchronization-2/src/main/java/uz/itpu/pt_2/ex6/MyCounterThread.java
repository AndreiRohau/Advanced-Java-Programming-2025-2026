package uz.itpu.pt_2.ex6;

/**
 * The MyCounterThread class describes a thread that changes the value of a counter a specified number of times.
 */
public class MyCounterThread extends Thread {
    private MyCounter counter;
    private int number;
    public MyCounterThread(MyCounter counter, int number) {
        this.counter = counter;
        this.number = number;
    }
    public void run() {
        for (int i = 0; i < number; i++)
            counter.increment();
    }
}
