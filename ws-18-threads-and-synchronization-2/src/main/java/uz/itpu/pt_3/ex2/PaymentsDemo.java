package uz.itpu.pt_3.ex2;

import java.util.concurrent.TimeUnit;

/**
 * To demonstrate a sequence of actions performed by threads in the example above,
 * the delay of the main thread is used by calling the sleep() method.
 * If, however, you remove the synchronization and calls to the wait() and notify() methods
 * in the application code, the payment result will most likely be zero
 * since it will be performed before replenishing the funds.
 *
 * When the notify() method is called, it sends a notification about the possibility of resuming work
 * to only one thread, which is randomly selected from all the threads waiting for it.
 * This means that the thread can reacquire the lock on the common object that it ceded by calling the wait() method.
 * The rest of the threads will remain in the WAITING state.
 * To see what this looks like, it is enough to run several payment threads in the previous example.
 */
public class PaymentsDemo {
    public static void main(String[] args) {
        Payment payment = new Payment();
        for (int i = 0; i < 5; i++) {
            new Thread(payment::doPayment).start();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        payment.init();
    }
}

/*
After calling the notify() method, only one randomly selected thread will acquire the lock, and
the remaining four will remain in the waiting state,
from which they can no longer be removed.
You can solve this problem by replacing the call to the notify() method
with a call to the notifyAll() method.
This will cause all threads suspended by calling the wait() method
on this object to receive a notification about the possibility
of acquiring a lock on the object and completing the payment.
The lock will not be acquired by all the threads at the same time.
The threads acquire the object's lock one by one but in
a random order as the synchronized methods complete their work.
 */
