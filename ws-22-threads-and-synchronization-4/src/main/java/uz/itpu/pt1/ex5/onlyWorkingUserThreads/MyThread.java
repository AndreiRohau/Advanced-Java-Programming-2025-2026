package uz.itpu.pt1.ex5.onlyWorkingUserThreads;

public class MyThread extends Thread {
    public void run() {
        for (int i = 0; i < 6; i++) {
            System.out.println(getName() + ", i=" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
