package uz.itpu.ex2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_OF_TASKS = 20;
        ExecutorService pool = Executors.newFixedThreadPool(4);

        TaskCallable[] tasks = new TaskCallable[NUM_OF_TASKS];
        Future[] futures = new Future[NUM_OF_TASKS];

        for (int i = 0; i < NUM_OF_TASKS; i++) {
            tasks[i] = new TaskCallable(i + 1); // init a task
            futures[i] = pool.submit(tasks[i]); // register to pool, getting back link
            System.out.println("Task_" + (i + 1) + " submitted." );
            Thread.sleep(1000);
        }

        Thread.sleep(2000);

        System.out.println("====".repeat(4));

        Thread.sleep(2000);

        for (int i = 0; i < NUM_OF_TASKS; ++i) {
            try {
                System.out.println(futures[i].get() + " ended");
            } catch (InterruptedException | ExecutionException ex) {
                throw new IllegalStateException();
            }
        }
        pool.shutdown();
    }
}

class TaskCallable implements Callable<String> {
    private int taskNumber;

    public TaskCallable(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String call() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Task_" + taskNumber + ": " + i);
            try {
                Thread.sleep((int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
        return "Task " + taskNumber;
    }
}


