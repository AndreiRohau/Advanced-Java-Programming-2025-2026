package uz.itpu;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorImplTimeoutTest {

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void testAdd_delta(Calculator calculator) {
        float expected = 2.5f;
        float actual = calculator.add(1, 2);
        assertEquals(expected, actual, 0.5f, "The actual value is not within the expected range");
    }

    // Timeout test EXAMPLE
    @Test
    @Order(3)
    public void testAddWithTimeoutNested(Calculator calculator) {
        assertTimeout(Duration.ofMillis(1000), () -> {Thread.sleep(100);});
    }
}
