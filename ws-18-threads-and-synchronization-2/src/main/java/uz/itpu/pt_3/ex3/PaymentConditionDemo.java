package uz.itpu.pt_3.ex3;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

/**
 * Here you can see interthread communication using a high-level threading API.
 * The Condition interface of the java.util.concurrent.locks package is used to manage locks.
 * You can only get an instance of the Condition type through the Lock type object and the newCondition() method.
 * The await() and signal() methods control the interaction between threads.
 * Their functionality is similar to how the wait() and notify() methods of the Object class act.
 */
public class PaymentConditionDemo {
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

class Payment {
    private int amount;
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();
    public void doPayment() {
        System.out.println("Start payment(lock)");
        try {
            lock.lock();
            while (amount <= 0) {
                condition.await();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        } finally {
            lock.unlock();
        }
        // payment code here
        System.out.println("Payment(lock) is closed");
    }
    public void init() {
        try {
            lock.lock();
            System.out.println("Init amount: ");
            amount = new Scanner(System.in).nextInt();
        } finally {
            condition.signal();
            lock.unlock();
        }
    }
}
