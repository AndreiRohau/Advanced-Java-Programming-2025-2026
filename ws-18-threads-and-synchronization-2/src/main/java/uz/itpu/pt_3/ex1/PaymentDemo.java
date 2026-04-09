package uz.itpu.pt_3.ex1;

import java.util.concurrent.TimeUnit;

/**
 * Here is an example of using these methods in the Payment class.
 * The thread making the payment evaluates the availability of funds and calls the wait() method
 * if there are none available.
 * The main thread calls the notify() method when the funds have been replenished.
 */
public class PaymentDemo {
    public static void main(String[] args) {
        Payment payment = new Payment();
        new Thread(payment::doPayment).start();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        payment.init();
    }
}
