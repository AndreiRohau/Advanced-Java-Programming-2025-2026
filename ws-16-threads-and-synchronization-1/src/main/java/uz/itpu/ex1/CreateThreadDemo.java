package uz.itpu.ex1;

public class CreateThreadDemo {

    public static void main(String[] args) {
        System.out.println("main start");
        TalkThread talk = new TalkThread();
        talk.start();
        System.out.println("main ended!");
    }
}

class TalkThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++)  {
            System.out.print(this.getName() + ": ");
            System.out.println("Talking " + (i + 1));
        }
    }
}