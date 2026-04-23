package uz.itpu.pt2.ex3;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParticipantLatch extends Thread {
    private Integer participantId;
    private int currentLotPrice;
    private int cash;
    private AuctionLatch auctionLatch;
    private CountDownLatch latchEndBid;
    private CountDownLatch latchAuctionBegin;
    private boolean lost;
    private int bid;

    public ParticipantLatch(int id, int lotPrice, int cash, AuctionLatch auctionLatch) {
        this.participantId = id;
        this.currentLotPrice = lotPrice;
        this.cash = cash;
        this.auctionLatch = auctionLatch;
        this.latchEndBid = auctionLatch.getLatchEndAuction();
        this.latchAuctionBegin = auctionLatch.getLatchAuctionBegin();
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public int getBid() {
        return bid;
    }

    /**
     * Participant thread logic:
     * 1. If cash is insufficient — mark as lost, signal ready, and exit without waiting for auction end.
     * 2. Otherwise — place a random bid above the current lot price, signal ready.
     * 3. Wait for the auction result latch, then continue independent work.
     */
    @Override
    public void run() {
        if (cash < currentLotPrice) {
            System.out.println("Participant #" + participantId
                    + " lost because (cash = " + cash + ") < (price = " + currentLotPrice + ")");
            lost = true;
            latchAuctionBegin.countDown();
            return; // no need to wait for auction end
        }

        System.out.println("Participant #" + participantId + " specifies a price. (cash = " + cash + ")");
        bid = currentLotPrice + new Random().nextInt(cash - currentLotPrice + 1);
        System.out.println("Made a bet Participant #" + participantId + " : " + bid);
        latchAuctionBegin.countDown();

        try {
            latchEndBid.await(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Only the winner pays their bid; losers get their money back
        boolean won = participantId.equals(auctionLatch.getWinnerId());
        int remainingCash = won ? cash - bid : cash;
        System.out.println("participant #" + participantId
                + " continue to work... (cash = " + remainingCash + ")");
    }
}
