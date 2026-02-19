package uz.itpu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
public class CalculatorImplParallelExecutionTest {

    @Disabled("Flacky test. Require fix.")
    @Test
    public void testAdd_1(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        try {
            Thread.sleep(5000); // Simulate a long-running operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testAdd_2(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        try {
            Thread.sleep(4000); // Simulate a long-running operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testAdd_3(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        try {
            Thread.sleep(4000); // Simulate a long-running operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testAdd_4(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        try {
            Thread.sleep(4000); // Simulate a long-running operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }
}
