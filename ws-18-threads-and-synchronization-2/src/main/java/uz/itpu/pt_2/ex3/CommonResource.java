package uz.itpu.pt_2.ex3;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This example is similar to the one above,
 * only the writing() method of the CommonResource class is described as synchronized.
 * When this program is executed, the writing to a file and
 * console output will be continuous and ordered: the first thread and then the second.
 */
public class CommonResource implements AutoCloseable {

    private FileWriter file;

    public CommonResource(String file) throws IOException {
        this.file = new FileWriter(file, true);
    }

    public synchronized void writing(String str, int i) {
        try {
            file.append(str + i);
            System.out.print(str + i);
            Thread.sleep((long)(Math.random() * 15));
            file.append("->" + str.charAt(0) + i + " ");
            System.out.print("->" + str.charAt(0) + i + " ");
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

