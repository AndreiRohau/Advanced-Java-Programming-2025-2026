package uz.itpu.ex3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * The UnaryActionDemo class creates a list of real values in the range [1.0 .. 1_000_000] using streams.
 * Then, it creates a task of the UnaryAction<Double> and passes it the list and
 * an operation of the UnaryOperator<Double> type as a lambda expression.
 * It then calls the invoke() method on the created task, which in turn calls the fork() and join() methods.
 * When completed, the list is printed to the console.
 */
public class UnaryActionDemo {
    public static void main(String[] args) {
        List<Double> numbers = DoubleStream.iterate(1.0, num -> num + 1)
                .limit(1_000_000)
                .boxed()
                .collect(Collectors.toList());
        new UnaryAction<>(numbers, d -> Math.sqrt(d)).invoke();
        numbers.stream().forEach(r -> System.out.printf("%7.4f %n ", r));
    }
}
