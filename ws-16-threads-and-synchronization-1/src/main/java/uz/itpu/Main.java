package uz.itpu;

public class Main {
    public static void main(String[] args) {
        System.out.println("main - start");
        Runnable runnable1 = () -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("Task 1 " + i);
            }
        };
        Runnable runnable2 = () -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("Task 2 " + i);
            }
        };
        runnable1.run();
        runnable2.run();
        System.out.println("main - end");
    }
}