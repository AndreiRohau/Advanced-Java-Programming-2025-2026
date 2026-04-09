package uz.itpu.pt_2.ex2;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This example describes the CommonResource class designed to work with text files.
 * The file field holds a reference to the data writing stream.
 * A stream is open for writing data at the end of a file
 * when an instance of the CommonResource type is created.
 * The writing() method of the CommonResource class writes the received data in a file and
 * then prints it to the console.
 * After that, its work is suspended for a random time quantum within 15 milliseconds.
 * After resuming, additional data is placed in the file and the console.
 */
public class CommonResource implements AutoCloseable {

    private FileWriter file;

    public CommonResource(String file) throws IOException {
        this.file = new FileWriter(file, true);
    }

    public void writing(String str, int i) {
        try {
            file.append(str + i);
            System.out.print(str + i);
            Thread.sleep((long)(Math.random() * 15));
            file.append("->" + str.charAt(0) + i + " " + "\n");
            System.out.print("->" + str.charAt(0) + i + " " + "\n");
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException();
        }
    }
    @Override
    public void close() throws IOException {
        if (file != null) {
            file.close();
        }
    }
}

