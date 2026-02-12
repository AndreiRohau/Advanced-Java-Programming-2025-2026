package uz.itpu.ajp;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorImplTest {

    private final Calculator calculator = new CalculatorImpl();

    @Test
    @DisplayName("add(): should add two numbers")
    void testAdd_positive() {
        // Arrange
        int a = 10;
        int b = 7;
        int expected = 17;

        // Act
        int actual = calculator.add(a, b);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("add(): should add two numbers")
    void testAdd_negative() {
        // Arrange
        int a = 10;
        int b = 7;
        int expected = 18;

        // Act
        int actual = calculator.add(a, b);

        // Assert
        assertNotEquals(expected, actual);
    }

    @Test
    @DisplayName("subtract(): should subtract second number from first")
    void testSubtract_positive() {
        // Arrange
        int a = 10;
        int b = 7;

        // Act
        int result = calculator.subtract(a, b);

        // Assert
        assertEquals(3, result);
    }

    @Test
    @DisplayName("multiply(): should multiply two numbers")
    void testMultiply_positive() {
        // Arrange
        int a = 6;
        int b = 7;

        // Act
        int result = calculator.multiply(a, b);

        // Assert
        assertEquals(42, result);
    }

    @Test
    @DisplayName("divide(): should throw when dividing by zero")
    void testDivide_negative() {
        // Arrange
        int a = 10;
        int b = 0;

        // Act + Assert
        assertThrows(ArithmeticException.class, () -> calculator.divide(a, b));
    }

    @Test
    @DisplayName("average(): should calculate rounded average")
    void testAverage_positive() {
        // Arrange
        List<BigDecimal> values = List.of(new BigDecimal("10"), new BigDecimal("11"), new BigDecimal("12"));

        // Act
        BigDecimal result = calculator.average(values, 2);

        // Assert
        assertEquals(new BigDecimal("11.00"), result);
    }

    @Test
    @DisplayName("average(): should throw when values is empty")
    void testAverage_negative() {
        // Arrange
        List<BigDecimal> values = List.of();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.average(values, 2));
    }
}
