package uz.itpu.pt1.ex4;

public class ThreadPriorityDemo {
    public static void main(String[] args) {
        PriorityThread minPriorityThread = new PriorityThread ();
        minPriorityThread.setName("Thread Min");
        minPriorityThread.setPriority(Thread.MIN_PRIORITY);
        PriorityThread maxPriorityThread = new PriorityThread ();
        maxPriorityThread.setName("Thread Max");
        maxPriorityThread.setPriority(Thread.MAX_PRIORITY);
        PriorityThread normalPriorityThread = new PriorityThread ();
        normalPriorityThread.setName("Thread Norm");
        normalPriorityThread.setPriority(Thread.NORM_PRIORITY);
        minPriorityThread.start();
        normalPriorityThread.start();
        maxPriorityThread.start();
    }
}

