package uz.itpu.pt_3.ex6;

/**
 * In the main thread—the main() method of the DeadLockDemo class—three threads of the MyThread type are created.
 * Each of the threads processes the same three objects of the MyObject type.
 */
public class DeadLockDemo {
    public static void main(String[] args) {

        MyObject object1 = new MyObject("Data 1");
        MyObject object2 = new MyObject("Data 2");
        MyObject object3 = new MyObject("Data 3");


        Thread thread1 = new MyThread("Thread_1",
                object1, object2, object3);
        Thread thread2 = new MyThread("Thread_2",
                object2, object3, object1);
        Thread thread3 = new MyThread("Thread_3",
                object3, object1, object2);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
/*
Since a virtual machine distributes processor time between threads,
the order of executing threads can be different in different launches.
*/
