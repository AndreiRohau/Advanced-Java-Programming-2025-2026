package uz.itpu.pt_2.ex5;

public class SynchroBlockThreadsDemo {
    public static void main(String[] args) {
        final StringBuilder string = new StringBuilder();

        Thread a = new Thread(() -> {
            synchronized (string) {
                int i = 0;
                while (i++ < 3) {
                    string.append("A");
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException();
                    }
                    System.out.println(string);
                }
            }
        });

        Thread b = new Thread(() -> {
            synchronized (string) {
                int j = 0;
                while (j++ < 3) {
                    string.append("B");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException();
                    }
                    System.out.println(string);
                }
            }
        });

        a.start();
        b.start();
    }
}

