package uz.itpu.pt2.ex2;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CyclicBarrier;

public class Auction {
    private final List<Participant> participants = new ArrayList<>();;
    private CyclicBarrier barrier;
    public Auction(int numberParticipant) {
        this.barrier = new CyclicBarrier(numberParticipant, Auction.this::defineWinner);
    }
    public CyclicBarrier getBarrier() {
        return barrier;
    }
    public void add(Participant e) {
        participants.add(e);
    }
    public void defineWinner() {
        Participant winner = Collections.max(participants, Comparator.comparingInt(Participant::getCurrentLotPrice));
        System.out.println("Participant #" + winner.getBidId() + ", price: " + winner.getCurrentLotPrice() + " win!");
        winner.setCash(winner.getCash() - winner.getCurrentLotPrice());
    }
}
