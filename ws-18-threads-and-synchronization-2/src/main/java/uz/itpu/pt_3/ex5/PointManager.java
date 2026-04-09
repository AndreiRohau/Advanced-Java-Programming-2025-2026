package uz.itpu.pt_3.ex5;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The PointManager class contains two methods for processing a point:
 *
 * The length() method for calculating the distance from a point to the origin
 * The randomChangePoint() method for changing the coordinates of a point
 */
public class PointManager {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    public double length(Point point) {
        double length = 0;
        String threadName = Thread.currentThread().getName();
        try {
            readLock.lock();
            System.out.println("Read begin: " + threadName);
            TimeUnit.MILLISECONDS.sleep(50);
            length = Math.hypot(point.getX(), point.getY());
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.printf("Read end: %16s %s %5.2f %n", threadName, point, length);
            return length;
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        } finally {
            readLock.unlock();
        }
    }
    public void randomChangePoint(Point point) {
        String threadName = Thread.currentThread().getName();
        try {
            writeLock.lock();
            System.out.println("writeLock begin: " + threadName + point);
            TimeUnit.MILLISECONDS.sleep(50);
            point.setX(point.getX() + (5 - new Random().nextInt(10)) / 2.0);
            point.setY(point.getY() + (5 - new Random().nextInt(10)) / 2.0);
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.println(" writeLock end: " + threadName + point);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        } finally {
            writeLock.unlock();
        }
    }
}
