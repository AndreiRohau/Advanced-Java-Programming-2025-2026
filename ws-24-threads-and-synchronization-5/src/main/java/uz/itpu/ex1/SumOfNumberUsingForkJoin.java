package uz.itpu.ex1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumOfNumberUsingForkJoin {
    private final static long N = 10_000_000L;
    private static final int NUM_THREADS = 10;

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(NUM_THREADS);
        long computedSum = pool.invoke(new RecursiveSumOfNumber(0, N));
        long formulaSum = (N * (N + 1)) / 2;
        System.out.printf("Sum for range 1..%d; computed sum = %d, formula sum = %d%n", N, computedSum, formulaSum);
    }

    static class RecursiveSumOfNumber extends RecursiveTask<Long> {
        private long from;
        private long to;

        public RecursiveSumOfNumber(long from, long to) { // [0; 1_000_000]
            this.from = from;
            this.to = to;
        }

        public Long compute() {
            long expectedLoadOfItemsPerThread = (N / NUM_THREADS); // 100k items per each of 10 threads

            // task fragmentation threshold
            if ((to - from) <= expectedLoadOfItemsPerThread) { // {1m} <= expectedItemsPerThread
                long localSum = 0;
                for (long number = from; number <= to; number++) {
                    localSum += number;
                }
                System.out.printf("\tSum of range %d to %d is %d%n", from, to, localSum);
                return localSum;
            } else {
                long middle = (from + to) / 2; // find the middle point of the range
                System.out.printf("Forking into two ranges: [%d ; %d] and [%d ; %d]%n", from, middle, middle + 1, to);

                RecursiveSumOfNumber firstHalf = new RecursiveSumOfNumber(from, middle); // [0; 500_000]
                firstHalf.fork(); // put first half of the task into the pool for execution

                RecursiveSumOfNumber secondHalf = new RecursiveSumOfNumber(middle + 1, to); // [500_001; 1_000_000]
                long resultSecondTask = secondHalf.compute(); // compute the second half of the task in the current thread recursively

                return firstHalf.join() + resultSecondTask; // join results when the first half is done
            }
        }
    }


}
