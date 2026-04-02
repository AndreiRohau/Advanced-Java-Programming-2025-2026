package uz.itpu.ex1;

public class Main {

    public static final int I = 20;

    public static void main(String[] args) {
        Runnable runnable = () -> {
            imitationOfProcess("Runnable");
        };

        Thread thread_1 = prepareNewThread("T_1");
        Thread thread_2 = prepareNewThread("T_2");
        
        thread_1.start();
        thread_2.start();
        runnable.run();
        
        imitationOfProcess("main");
        System.out.println("main is over");
    }

    private static void imitationOfProcess(String threadTag) {
        for (int i = 0; i < I; i++) {
            System.out.println(threadTag + " : ------------ " + i);
        }
        System.out.println(threadTag + " : is done");
    }

    public static Thread prepareNewThread(String threadTag) {
        Thread thread = new Thread(() -> imitationOfProcess(threadTag));
        return thread;
    }
}
