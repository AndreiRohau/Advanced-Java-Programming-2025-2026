package uz.itpu.ex2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

class FileSystemAnalysisDemo {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        File dir = new File(sc.nextLine());
        String word = sc.nextLine();

        ExecutorService pool = Executors.newCachedThreadPool();
        CounterFiles task = new CounterFiles(dir, word, pool);
        Future<Integer> result = pool.submit(task);

        System.out.println(result.get());
        pool.shutdown();
    }
}

class CounterFiles implements Callable<Integer> {
    private File dir;
    private String word;
    private ExecutorService pool;
    public CounterFiles(File dir, String word, ExecutorService pool) {
        this.dir = dir;
        this.word = word;
        this.pool = pool;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;
        File[] files = dir.listFiles();
        List<Future<Integer>> results = new ArrayList<>();

        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    results.add(pool.submit(new CounterFiles(f, word, pool)));
                } else {
                    if (search(f)) count++;
                }
            }
        }

        for (Future<Integer> f : results) {
            count += f.get();
        }
        return count;
    }

    public boolean search(File f) {
        try (Scanner in = new Scanner(f)) {
            while (in.hasNextLine()) {
                if (in.nextLine().contains(word)) return true;
            }
        } catch (IOException e) {}
        return false;
    }
}

