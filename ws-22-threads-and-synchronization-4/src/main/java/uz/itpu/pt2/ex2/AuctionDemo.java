package uz.itpu.pt2.ex2;

import java.util.Random;

public class AuctionDemo {
    public static void main(String[] args) {
        int numberParticipant = 5;
        Auction auction = new Auction(numberParticipant);
        int startPrice = 50;
        System.out.println("Start price : " + startPrice);
        for (int num = 0; num < numberParticipant; num++) {
            int cash = 100 + new Random().nextInt(50);
            Participant participant = new Participant(num, startPrice, cash, auction);
            auction.add(participant);
            participant.start();
        }
    }
}
