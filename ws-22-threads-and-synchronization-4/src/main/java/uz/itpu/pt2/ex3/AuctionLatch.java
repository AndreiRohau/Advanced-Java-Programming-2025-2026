package uz.itpu.pt2.ex3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class AuctionLatch extends Thread {
    private List<ParticipantLatch> participants = new ArrayList<>();
    private CountDownLatch latchEndAuction = new CountDownLatch(1);
    private CountDownLatch latchAuctionBegin;
    /** ID of the winning participant; -1 means no winner (auction cancelled). */
    private volatile int winnerId = -1;
    public AuctionLatch(int numbersParticipant) {
        latchAuctionBegin = new CountDownLatch(numbersParticipant);
    }
    public void add(ParticipantLatch e) {
        participants.add(e);
    }
    public CountDownLatch getLatchEndAuction() {
        return latchEndAuction;
    }
    public CountDownLatch getLatchAuctionBegin() {
        return latchAuctionBegin;
    }

    /** Returns the winner's participant ID, or -1 if the auction was cancelled. */
    public int getWinnerId() {
        return winnerId;
    }

    /**
     * Auction thread logic:
     * 1. Announce waiting for participants.
     * 2. Await until all participants have either placed a bid or dropped out (latchAuctionBegin reaches zero).
     * 3. Determine the winner by the highest bid among non-lost participants.
     * 4. Announce the winner, then open the end-of-auction latch so participants can continue.
     */
    @Override
    public void run() {
        System.out.println("Waiting for participants to bet...");
        try {
            latchAuctionBegin.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        List<ParticipantLatch> bidders = participants.stream()
                .filter(p -> p.getBid() > 0)
                .collect(Collectors.toList());

        if (bidders.isEmpty()) {
            System.out.println("No participants placed a bid. Auction cancelled.");
        } else {
            ParticipantLatch winner = Collections.max(bidders,
                    Comparator.comparingInt(ParticipantLatch::getBid));
            winnerId = winner.getParticipantId();
            System.out.println("Participant #" + winnerId
                    + ", price: " + winner.getBid() + " win!");
        }

        latchEndAuction.countDown();
    }
}
