package uz.itpu.ex2;

import java.util.concurrent.*;

public class ThreadpoolFactory {
    public static void main(String[] args) {

        // Creates working threads as necessary; idle threads exist for one minute
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // Creates a pool with a fixed number of constantly working threads; idle threads are not destroyed
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);

        // Creates a pool with a single thread that performs tasks sequentially
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

        // Creates a pool of tasks that allows tasks to be executed according to a given schedule
        ExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(3);

        // Creates a pool with a single thread that allows tasks to be executed according to a given schedule
        ExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        // custom
        custom();
    }

    private static void custom() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(20);
        // The number of pool worker threads that persist even if they are idle
        // The maximum number of threads in the pool
        // The maximum time that idle threads wait for tasks and then terminate
        // The unit of time for the keepAliveTime parameter
        // The line of tasks before they are sent for execution; only contains tasks of the Runnable type
        ExecutorService executorService = new ThreadPoolExecutor(2, 3, 4, TimeUnit.MILLISECONDS, workQueue);
    }
}
