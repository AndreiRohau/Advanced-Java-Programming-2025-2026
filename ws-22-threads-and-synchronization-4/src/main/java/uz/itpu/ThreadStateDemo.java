package uz.itpu;

public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                // Moving to TIMED_WAITING
                Thread.sleep(3000);

                // Moving to WAITING by waiting for another thread
                synchronized (ThreadStateDemo.class) {
                    ThreadStateDemo.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 1. NEW: Created but not yet started
        System.out.println("1. NEW: " + thread1.getState());

        thread1.start();
        // 2. RUNNABLE: After start() is called
        System.out.println("2. RUNNABLE: " + thread1.getState());

        Thread.sleep(1000);
        // 3. TIMED_WAITING: While thread is in Thread.sleep()
        System.out.println("3. TIMED_WAITING: " + thread1.getState());

        Thread.sleep(3000);
        // 4. WAITING: While thread is in wait()
        System.out.println("4. WAITING: " + thread1.getState());

        // Wake it up to let it finish
        synchronized (ThreadStateDemo.class) {
            ThreadStateDemo.class.notify();
        }

        thread1.join();
        // 5. TERMINATED: After execution finishes
        System.out.println("5. TERMINATED: " + thread1.getState());
    }
}

