package uz.itpu.ex1;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateCallableDemo {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = IntStream.range(0, 1_000)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("point 1");
        FutureTask<Integer> task = new FutureTask<>(new ActionCallable(list));
        System.out.println("point 2");
        new Thread(task).start();

        Thread.sleep(100);
        System.out.println("point 3");

        try {
            System.out.println("point 4");
            System.out.println(task.get());
            System.out.println("point 5");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ActionCallable implements Callable<Integer> {
    private List<Integer> integers;

    public ActionCallable(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    public Integer call() {
        System.out.println("callable started");
        int sum = 0;
        for (int number : integers) {
            sum += number;
        }
        System.out.println("callable finished");
        return sum;
    }
}
