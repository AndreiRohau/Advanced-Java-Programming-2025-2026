package uz.itpu.pt1.ex2;

import java.util.concurrent.TimeUnit;

class JoinThread extends Thread {
    public void run() {
        System.out.println("START");
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        System.out.println("END");
    }
}
