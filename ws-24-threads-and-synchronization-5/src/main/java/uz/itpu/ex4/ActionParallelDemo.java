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
            int result = pool.submit(task).get(); // parallel() understands it's place and is using ForkJoinPool.commonPool()
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException();
        }
        System.out.println((System.currentTimeMillis() - sec) / 1000.);
    }
}
/*
In Java, a Stream is sequential by default. Think of the ForkJoinPool as a "house" full of workers.
The .parallel() call is like giving those workers a command: "Break this blueprint into pieces and work on them together."
Without this command, a single worker will take the entire blueprint and work on it alone from start to finish,
even if there are seven other colleagues sitting idle in the next room.

An important point: .parallel() acts like a "toggle switch."
It tells the stream to start creating internal RecursiveTask objects and calling
those same fork() and join() methods we discussed earlier.
 */
