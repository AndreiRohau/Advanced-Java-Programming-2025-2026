package uz.itpu.ex2;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerDemo {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerCounter(), 10000, 6000);

        Thread.sleep(30000);
        timer.cancel();
    }
}

class TimerCounter extends TimerTask {

    private static int i;

    @Override
    public void run() {
        System.out.print(++i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        System.out.println("\t" + i);
    }
}
