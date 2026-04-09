package uz.itpu.pt_1.ex1;

/**
 * Why you see Errors (The Race Condition) ???
 *
 * The line varVolat = varNonVolat = ++localValue; is not atomic.
 * The JVM executes it in multiple distinct steps:
 * 1. Increment localValue.
 * 2. Store the new value into varNonVolat.
 * 3. Store the new value into varVolat.
 *
 * So ChangeListener thread can read the values between step 2 and step 3.
 * In that tiny window, varNonVolat has been updated, but varVolat still holds the old value.
 */
public class VolatileDemo {

    private static volatile int varVolat = 0;
    private static int varNonVolat = 0;

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
                varVolat = varNonVolat = ++localValue;
            }
        }
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                if (varVolat != varNonVolat) {
                    System.out.println("Error: " + varVolat + " != " + varNonVolat);
                }
            }
        }
    }
}
