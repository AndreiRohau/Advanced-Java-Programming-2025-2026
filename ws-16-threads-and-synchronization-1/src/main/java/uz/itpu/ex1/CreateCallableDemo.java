package uz.itpu.ex1;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateCallableDemo {
    public static void main(String[] args) {
        List<Integer> list = IntStream.range(0, 1_000)
                .boxed()
                .collect(Collectors.toList());
        FutureTask<Integer> task = new FutureTask<>(new ActionCallable(list));
        new Thread(task).start();
        try {
            System.out.println(task.get());
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
        int sum = 0;
        for (int number : integers) {
            sum += number;
        }
        return sum;
    }
}
