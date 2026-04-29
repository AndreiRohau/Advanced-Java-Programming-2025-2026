package uz.itpu.ex4;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * implicit parallelism
 *
 * A custom thread based on the Runnable or Callable types cannot be executed in parallel,
 * but it can be wrapped in a ForkJoinPool.
 *
 * Running the code will show that the following threads are being used:
 *
 * ForkJoinPool-1-worker-21
 * ForkJoinPool-1-worker-7
 * Thus, the parallel streams use the fork/join threads as parents, not ordinary threads.
 * As a result, ForkJoinPool.commonPool is not applied.
 *
 * // remove peek before measures
 * // 100m | np | 4.755s
 * // 100m | p | 1.119s
 */
public class ActionParallelDemo {
    public static void main(String[] args) {
        long sec = System.currentTimeMillis();
        Callable<Integer> task = () -> IntStream.range(0, 100_000_000)
                .boxed()
                .parallel()
                .map(x -> x / 3)
//                .peek(th -> System.out.println(Thread.currentThread().getName()))
                .reduce((x, y) -> x + (int)(3 * Math.sin(y)))
                .get();
        ForkJoinPool pool = new ForkJoinPool(8);
        try {
            int result = pool.submit(task).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException();
        }
        System.out.println((System.currentTimeMillis() - sec) / 1000.);
    }
}
