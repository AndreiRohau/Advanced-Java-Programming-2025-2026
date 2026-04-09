package uz.itpu.pt_3.ex1;

import java.util.Scanner;

public class Payment {
    private int amount;
    public synchronized void doPayment() {
        System.out.println("Start payment");
        while (amount <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
        // payment code
        System.out.println("Payment is closed");
    }

    public synchronized void init() {
        System.out.println("Init amount:");
        amount = new Scanner(System.in).nextInt();
        this.notify();
    }
}
