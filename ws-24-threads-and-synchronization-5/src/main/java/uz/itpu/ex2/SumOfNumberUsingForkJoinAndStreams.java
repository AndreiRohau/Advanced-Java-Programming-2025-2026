package uz.itpu.ex2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumOfNumberUsingForkJoinAndStreams {
    private final static long N = 1_000_000L;
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

        public RecursiveSumOfNumber(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public Long compute() {
            if ((to - from) <= 100_000) {
                long localSum = 0;
                for (long number = from; number <= to; number++) {
                    localSum += number;
                }
                return localSum;
            } else {
                long middle = (from + to) / 2;
                List<RecursiveSumOfNumber> subTasks = new ArrayList<>();
                subTasks.add(new RecursiveSumOfNumber(from, middle));
                subTasks.add(new RecursiveSumOfNumber(middle + 1, to));
                subTasks.stream().forEach(RecursiveTask::fork);
                return subTasks.stream()
                        .map(RecursiveTask::join)
                        .reduce((res1, res2) -> res1 + res2)
                        .orElse(0L);
            }
        }
    }


}
