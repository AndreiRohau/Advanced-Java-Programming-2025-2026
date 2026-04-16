package uz.itpu.pt_1.e3_synchronizer;

import java.util.concurrent.Exchanger;

/**
 * Think of this as a hand-off.
 * It allows two threads to wait for each other and then swap a piece of data.
 */
public class ExchangerExample {
    public static void main(String[] args) {
        Exchanger<String> box = new Exchanger<>();

        new Thread(() -> {
            try {
                String data = "Thread A's Secret";
                System.out.println("A swapping: " + data);
                data = box.exchange(data); // Send A's, receive B's
                System.out.println("A received: " + data);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();

        new Thread(() -> {
            try {
                String data = "Thread B's Secret";
                System.out.println("B swapping: " + data);
                data = box.exchange(data); // Send B's, receive A's
                System.out.println("B received: " + data);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();
    }
}

