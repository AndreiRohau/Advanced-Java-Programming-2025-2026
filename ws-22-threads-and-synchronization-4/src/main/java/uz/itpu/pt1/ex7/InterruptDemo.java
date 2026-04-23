package uz.itpu.pt1.ex7;

public class InterruptDemo {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        thread.interrupt();
    }
}
