package uz.itpu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

//@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
public class CalculatorImplTimeoutTest {

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void testAddWithTimeout(Calculator calculator) {
        try {
            Thread.sleep(100); // Simulate a long-running operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
    }

    // Timeout test EXAMPLE
    @Test
    public void testAddWithTimeoutNested(Calculator calculator) {
        assertTimeout(Duration.ofMillis(1000), () -> {Thread.sleep(100);});
    }
}
