package uz.itpu.pt_1.e4_executor;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

/**
 * This is for "Divide and Conquer" tasks.
 * It splits a huge task into tiny sub-tasks (Fork)
 * until they are small enough to run, then merges the results (Join).
 */
public class ForkJoinExample extends RecursiveTask<Integer> {
    private final int n;
    ForkJoinExample(int n) { this.n = n; }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n; // Task is small enough
        }

        ForkJoinExample f1 = new ForkJoinExample(n - 1);
        f1.fork(); // Run sub-task in parallel

        ForkJoinExample f2 = new ForkJoinExample(n - 2);
        return f2.compute() + f1.join(); // Combine results
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println("Fibonacci: " + pool.invoke(new ForkJoinExample(10)));
    }
}

