package uz.itpu.ex3;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.UnaryOperator;

/**
 * The UnaryAction<T> class is a thread of the RecursiveAction type.
 * When creating an object of this class, you specify a list of real values,
 * an operation for processing the list elements, and the range of elements to be processed.
 * The compute() method determines if the number of elements is less than the THRESHOLD value.
 * If yes, then, execution of the obtained operation with the specified elements of the list begins.
 * If the number of list elements is large, it is divided into two parts;
 * two subtasks are created, and the invokeAll() method is called to execute them.
 * @param <T>
 */
public class UnaryAction<T> extends RecursiveAction {
    private List<T> subjectList;
    private UnaryOperator<T> operator;
    private int begin;
    private int end;
    private static final int THRESHOLD = 100_000;
    public UnaryAction(List<T> subjectList, UnaryOperator<T> operator, int begin, int end) {
        this.operator = operator;
        this.subjectList = subjectList;
        this.begin = begin;
        this.end = end;
    }
    public UnaryAction(List<T> subjectList, UnaryOperator<T> operator) {
        this(subjectList, operator, 0, subjectList.size());
    }
    @Override
    protected void compute() {
        if (end - begin < THRESHOLD) {
            System.out.printf("from %d to %d - thread %s%n", begin, end,
                    Thread.currentThread().getName());
            for (int i = begin; i < end; i++) {
                subjectList.set(i, operator.apply(subjectList.get(i)));
            }
        } else {
            int middle = (begin + end) / 2;
            invokeAll(new UnaryAction<T>(subjectList, operator, begin, middle),
                    new UnaryAction<T>(subjectList, operator, middle, end));
        }
    }
}