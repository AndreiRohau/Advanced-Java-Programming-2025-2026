package uz.itpu.pt_1.market;

import java.util.concurrent.TimeUnit;
public class Broker extends Thread {
    private static Market market;
    private static final int PAUSE_IN_MILLIS = 500;
    public static void initMarket(Market market) {
        Broker.market = market;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Current index: " + market.getIndex());
                TimeUnit.MILLISECONDS.sleep(PAUSE_IN_MILLIS);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }
}
