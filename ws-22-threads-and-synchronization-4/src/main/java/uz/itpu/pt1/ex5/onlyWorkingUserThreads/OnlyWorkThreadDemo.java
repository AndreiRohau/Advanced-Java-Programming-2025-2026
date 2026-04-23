package uz.itpu.pt1.ex5.onlyWorkingUserThreads;

public class OnlyWorkThreadDemo {
    public static void main(String[] args) {
        new MyThread().start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method main() finished");
    }
}
