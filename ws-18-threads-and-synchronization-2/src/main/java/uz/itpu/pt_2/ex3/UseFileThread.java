package uz.itpu.pt_2.ex3;

/**
 * The UseFileThread class is a thread that receives a reference and
 * name for an object of the CommonResource type when it is created.
 * When executed, the thread calls the writing() method in a loop at each iteration.
 * The thread passes the method the name and iteration number, which are written in a file.
 */
public class UseFileThread extends Thread {

    private CommonResource resource;

    public UseFileThread(String name, CommonResource resource) {
        super(name);
        this.resource = resource;
    }
    public void run() {
        for (int i = 0; i < 5; i++)
            resource.writing(this.getName(), i);
    }
}
