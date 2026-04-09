package uz.itpu.pt_3.ex6;

/**
 * A thread of the MyThread type acquires a lock on three objects of the MyObject type in order
 * when it calls the order() method on them.
 * When blocking the first object, notice that the third object is passed to the method.
 * When blocking the second object, the first object is passed to the method, and while blocking the third object,
 * the second object is passed to the method.
 * Thus, by using locks on various objects, the program tries to avoid a deadlock.
 * However, this is not always possible.
 */
public class MyThread extends Thread {
    String threadName;
    MyObject obj1;
    MyObject obj2;
    MyObject obj3;
    public MyThread(String str, MyObject obj1, MyObject obj2, MyObject obj3) {
        threadName = str;
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj3 = obj3;
    }
    public void run() {
        synchronized(obj1) {
            obj1.order(obj3);
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }

        synchronized(obj2) {
            obj2.order(obj1);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }

        synchronized(obj3) {
            obj3.order(obj2);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }
}
