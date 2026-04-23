package uz.itpu.pt2.ex3;

import java.util.Random;

public class LatchDemo {
    public static void main(String[] args) {
        int numbersParticipant = 5;
        AuctionLatch auction = new AuctionLatch(numbersParticipant);
        int startPrice = 110;
        auction.start();
        for (int num = 0; num < numbersParticipant; num++) {
            int cash = 100 + new Random().nextInt(20);
            ParticipantLatch participant = new ParticipantLatch(num, startPrice, cash, auction);
            auction.add(participant);
            participant.start();
        }
    }
}
