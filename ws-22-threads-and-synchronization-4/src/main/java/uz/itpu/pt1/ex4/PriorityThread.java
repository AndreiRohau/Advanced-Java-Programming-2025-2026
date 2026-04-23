package uz.itpu.pt1.ex4;

public class PriorityThread extends Thread {
    public void run() {
        for (int i = 0; i < 4; i++)
            System.out.println("Thread: " + getName() + " i=" + i);
    }
}
