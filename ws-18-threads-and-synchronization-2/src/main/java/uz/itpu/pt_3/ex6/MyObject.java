package uz.itpu.pt_3.ex6;

/**
 * The MyObject class has two methods:
 *
 * The order() method—a regular instance method
 * The reply() method—a synchronized instance method
 * The reply() method simply outputs to the console the name of the thread and what object the thread has accessed.
 *
 * The order() method, which is called by a thread for the object on which the thread has acquired a lock,
 * receives another object of the MyObject type.
 * Next, the method displays to the console the name of the thread and
 * the name of the object for which it was called, i.e., the name of the current object.
 * Then, it calls the reply() method on the object received in the parameter to acquire a lock on it as well.
 */
public class MyObject {
    public final String name;

    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void order(MyObject object) {
        System.out.println(((MyThread) Thread.currentThread())
                .threadName + " Holding lock " + this.name + "...");
        object.reply();
    }

    public synchronized void reply() {
        System.out.println(((MyThread) Thread.currentThread())
                .threadName + " Got lock " + this.name + "...");
    }
}
