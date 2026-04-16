package uz.itpu.pt_1.e4_executor;

import java.util.concurrent.*;

/**
 * A Runnable just runs and stops.
 * A Callable is like a Runnable, but it returns a value and can throw exceptions.
 * A Future is the "receipt" you get back while the thread is still working.
 */
public class CallableExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit a task that returns a String
        Future<String> futureResult = executor.submit(() -> {
            Thread.sleep(1000); // Simulate work
            return "Task Finished!";
        });

        System.out.println("Waiting for result...");

        // This blocks until the thread is done and returns the value
        String result = futureResult.get();
        System.out.println("Result: " + result);

        executor.shutdown();
    }
}
