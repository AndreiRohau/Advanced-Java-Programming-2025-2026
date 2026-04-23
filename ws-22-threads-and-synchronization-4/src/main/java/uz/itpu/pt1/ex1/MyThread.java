package uz.itpu.pt1.ex1;

class MyThread extends Thread {
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread: " + getName() + " i=" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
    }
}