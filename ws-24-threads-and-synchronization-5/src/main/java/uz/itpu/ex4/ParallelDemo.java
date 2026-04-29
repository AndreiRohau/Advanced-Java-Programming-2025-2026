package uz.itpu.ex4;

import java.util.stream.LongStream;

/**
 * explicit parallelism
 *
 * During execution, the application prints the names of processing threads to the console in the following format:
 *
 * ForkJoinPool.commonPool-worker-5
 * ForkJoinPool.commonPool-worker-11
 * If you remove the call of the parallel() method,
 * then only the name of the main thread will be displayed during execution.
 * There will be no division into subtasks.
 *
 * // remove peek before measures
 * // 10m | np | 0.5s
 * // 100m | np | 6.4s
 * // 10m | p | 0.25s
 * // 100m | p | 2.3s
 */
public class ParallelDemo {
    public static void main(String[] args) {
        long sec = System.currentTimeMillis();
        long result;
        result = LongStream.range(0, 100_000_000)
                .boxed()
                .parallel()
                .map(x -> x / 7)
//                .peek(v -> System.out.println(Thread.currentThread().getName() + " - " + v))
                .reduce((x,y) -> x + (int) (3 * Math.sin(y)))
                .get();
        System.out.println(result);
        System.out.println((System.currentTimeMillis() - sec) / 1000.);
    }
}
