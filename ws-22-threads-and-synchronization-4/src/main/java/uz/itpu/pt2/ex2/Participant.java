package uz.itpu.pt2.ex2;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Participant extends Thread {
    private Integer participantId;
    private int currentLotPrice;
    private int cash;
    private Auction auction;

    public Participant(int id, int currentLotPrice, int cash, Auction auction) {
        this.participantId = id;
        this.currentLotPrice = currentLotPrice;
        this.cash = cash;
        this.auction = auction;
    }

    public Integer getBidId() {
        return participantId;
    }

    public int getCurrentLotPrice() {
        return currentLotPrice;
    }

    public void setCurrentLotPrice(int currentLotPrice) {
        this.currentLotPrice = currentLotPrice;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    @Override
    public void run() {
        try {
            // Participant specifies a price
            System.out.println("Participant #" + participantId + " specifies a price. (cash = " + cash + ")");
//            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));

            // Place a bid: raise the price by a random amount if affordable
            int bid = currentLotPrice + new Random().nextInt(20);
            if (bid <= cash) {
                currentLotPrice = bid;
            }
            System.out.println("Auction Participant #" + participantId + ": " + currentLotPrice);

            // Wait at the barrier for all participants to place their bids
            auction.getBarrier().await(15, TimeUnit.SECONDS);

            // Continue work after the barrier opens
            System.out.println("Participant #" + participantId + " continue to work after the barrier opens... (cash = " + cash + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Participant #" + participantId + " was interrupted.");
        } catch (BrokenBarrierException e) {
            System.out.println("Participant #" + participantId + ": barrier was broken.");
        } catch (Exception e) {
            System.out.println("Participant #" + participantId + ": " + e.getMessage());
        }
    }
}
