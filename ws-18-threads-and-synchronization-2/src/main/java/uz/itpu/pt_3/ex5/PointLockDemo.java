package uz.itpu.pt_3.ex5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The PointLockDemo class is the main thread on which the thread pool is created.
 * Then, the thread pool gets 15 threads to execute.
 * When created, each thread receives a point, the PointManager object for processing it, and
 * a condition for what operation to perform with the point.
 * The point processing condition is determined randomly using an object of the Random type.
 */
public class PointLockDemo {
    public static void main(String[] args) {
        PointManager pointManager = new PointManager();
        Random rand = new Random();
        ExecutorService service = Executors.newFixedThreadPool(15);
        Point point = new Point(1, -1);
        for (int i = 0; i < 15; i++) {
            service.submit(new PointThread(pointManager, point, rand.nextBoolean()));
        }
        service.shutdown();
    }
}
/*
As you can see from the output to the console,
when one thread starts changing the coordinates of a point,
the other threads are locked until this operation is completed.
When one thread starts calculating the distance from a point to the origin,
only the thread that wants to change the coordinates of the point is locked.
Other threads that also want to calculate the distance can do so.


 */
