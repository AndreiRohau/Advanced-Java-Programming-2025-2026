package uz.itpu.pt1.ex5.workingUserThreadsAndDeamonThreads;

public class DaemonDemo {
    public static void main(String[] args) {
        MyThread daemonThread = new MyThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method main() finished");
    }
}
