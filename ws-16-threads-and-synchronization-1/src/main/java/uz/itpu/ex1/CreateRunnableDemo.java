package uz.itpu.ex1;

public class CreateRunnableDemo {

    public static void main(String[] args) {
        System.out.println("main start");
        Thread walk = new Thread(new WalkThread());
        walk.start();
        System.out.println("main ended!");
    }
}

class WalkThread implements Runnable {
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (int i = 0; i < 5; i++) {
            System.out.println(name + ": Walking " + (i+1));
        }
    }
}
