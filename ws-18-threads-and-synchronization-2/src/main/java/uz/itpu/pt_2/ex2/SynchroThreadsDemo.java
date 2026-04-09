package uz.itpu.pt_2.ex2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * In the main() method of the SynchroThreadsDemo class — the main thread —
 * an object of the CommonResource type is created and connected to the "thread.txt" file.
 * Then, two threads are created and run to work with this file.
 * After that, the main thread is suspended for two milliseconds.
 */
public class SynchroThreadsDemo {
    public static void main(String[] args) {
        try (CommonResource resource = new CommonResource("thread.txt")) {
            UseFileThread thr1 = new UseFileThread("First", resource);
            UseFileThread thr2 = new UseFileThread("Second", resource);
            thr1.start();
            thr2.start();
            TimeUnit.SECONDS.sleep(2);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException();
        }
        System.out.println("As a result, both threads write their data to the same file and " +
                "the output to the console at the same time, resulting in erratic and unpredictable writing. ");
    }
}
