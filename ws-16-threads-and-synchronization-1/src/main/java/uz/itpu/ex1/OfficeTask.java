package uz.itpu.ex1;

import java.util.concurrent.*;

public class OfficeTask {
    public static void main(String[] args) throws Exception {
        // 1. The Manager (Pool of 2 Threads/Cores)
        ExecutorService manager = Executors.newFixedThreadPool(2);

        // 2. The Callable (The Task)
        Callable<String> fetchReport = () -> {
            System.out.println("Preparing a report");
            Thread.sleep(2000); // Simulating 2 seconds of work
            System.out.println("Report is Ready!");
            return "Annual Report 2024";
        };

        System.out.println("Boss: Ordering the report...");

        // 3. The Future (The Receipt)
        // We submit the task and immediately get a 'Future' back.
        Future<String> receipt = manager.submit(fetchReport);

        // The Boss can do other things here (Concurrency!)
        Thread.sleep(100); // Simulating 2 seconds of work // todo commit uncommit it to check difference
        System.out.println("Boss: Drinking coffee while waiting...");

        Thread.sleep(4000); // Simulating 2 seconds of work
        // 4. Getting the result
        // .get() is 'blocking'. The Boss stops here until the report is ready.
        String result = receipt.get();

        System.out.println("Boss: Got it! Result is: " + result);

        manager.shutdown();
    }
}

