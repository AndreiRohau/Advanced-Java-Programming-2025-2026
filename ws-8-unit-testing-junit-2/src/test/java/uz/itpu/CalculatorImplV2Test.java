package uz.itpu;

// JUnit 4  approach example

/*
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class CalculatorImplV2Test {

    @org.junit.BeforeClass
    public static void beforeClass() {
        System.out.println("Before all tests");
    }
    @org.junit.AfterClass
    public static void afterClass() {
        System.out.println("After all tests");
    }
    @org.junit.Before
    public void before() {
        System.out.println("Before each...");
    }
    @org.junit.After
    public void after() {
        System.out.println("After each...");
    }

    private static Object[] testValues() {
        return new Object[] {
                        new Object[] {1, 2, 3},
                        new Object[] {5, 10, 15},
                        new Object[] {0, 0, 0},
                        new Object[] {-5, -5, -10}
        };
    }

    @Test
    @Parameters(method = "testValues")
    public void testAdd(int num1, int num2, int expected) {
        // Arrange
        Calculator calculator = new CalculatorImpl();
        // Act
        int actual = calculator.add(num1, num2);
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Parameters({
            "1, 1, 2",
            "5, 5, 10",
            "10, 15, 25",
            "100, 200, 300"
    })
    public void shouldPerformCalculationsWithMethodSource(int num1, int num2, int expected) {
        // Arrange
        Calculator calculator = new CalculatorImpl();
        // Act
        int actual = calculator.add(num1, num2);
        // Assert
        assertEquals(expected, actual);
    }
}
*/