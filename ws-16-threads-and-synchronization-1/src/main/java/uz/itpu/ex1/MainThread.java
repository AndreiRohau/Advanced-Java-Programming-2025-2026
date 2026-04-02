package uz.itpu.ex1;

public class MainThread extends Thread {
    public void run(Thread th) {              // line 1
        System.out.print("R");
    }

    public static void main(String... args) { // line 2
        new Thread(new MainThread()).start(); // line 3
    }
}
