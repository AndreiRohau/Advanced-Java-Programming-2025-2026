package uz.itpu.pt1.ex7;

public class MyThread extends Thread {
    public void run() {
        int i = 1;
        while( !isInterrupted() )  {
            System.out.println("Thread:" + getName()+ " i=" + i++);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
