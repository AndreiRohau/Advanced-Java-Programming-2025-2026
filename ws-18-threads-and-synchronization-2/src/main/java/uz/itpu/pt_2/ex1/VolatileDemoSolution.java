package uz.itpu.pt_2.ex1;

/**
 * To fix the race condition,
 * you must ensure that
 * the write (updating both variables) and
 * the read (comparing both variables)
 * are atomic—meaning - no other thread can "sneak in" while they are happening
 */
public class VolatileDemoSolution {

    private static int varVolat = 0; // volatile is no longer needed with synchronization
    private static int varNonVolat = 0;
    private static final Object lock = new Object(); // The shared lock

    public static void main(String[] args) {
        ChangeListener listener = new ChangeListener();
        ChangeMaker maker = new ChangeMaker();
        listener.start();
        maker.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        listener.interrupt();
        maker.interrupt();
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {
            int localValue = 0;
            while (!isInterrupted()) {
                // Synchronize the write operation
                synchronized (lock) {
                    varVolat = varNonVolat = ++localValue;
                }
            }
        }
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                // Synchronize the read operation
                synchronized (lock) {
                    if (varVolat != varNonVolat) {
                        System.out.println("Error: " + varVolat + " != " + varNonVolat);
                    }
                }
            }
        }
    }
}
