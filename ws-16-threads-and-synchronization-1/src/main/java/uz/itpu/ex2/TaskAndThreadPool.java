package uz.itpu.ex2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskAndThreadPool implements Runnable {
    private String taskInfo;
    public TaskAndThreadPool(String taskInfo) {
        this.taskInfo = taskInfo;
    }
    @Override
    public void run() {
        System.out.println(taskInfo);
    }

    public static void main(String[] args) {
        ExecutorService tpe = new ThreadPoolExecutor(5, 10, 30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        TaskAndThreadPool[] tasks = new TaskAndThreadPool[10];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new TaskAndThreadPool("Task " + i);
            tpe.execute(tasks[i]);
        }
        tpe.shutdown();
    }
}
