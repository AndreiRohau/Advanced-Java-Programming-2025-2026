package uz.itpu.ajp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * A tiny utility class with a few arithmetic operations.
 * <p>
 * This class is intentionally small and readable for unit-testing practice.
 */
public class CalculatorImpl implements Calculator {

    /**
     * Adds two integers.
     *
     * @param a first value
     * @param b second value
     * @return {@code a + b}
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts one integer from another.
     *
     * @param a left value
     * @param b right value
     * @return {@code a - b}
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two integers.
     *
     * @param a first value
     * @param b second value
     * @return {@code a * b}
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides {@code a} by {@code b}.
     *
     * @param a dividend
     * @param b divisor
     * @return integer division result
     * @throws ArithmeticException when {@code b == 0}
     */
    public int divide(int a, int b) {
        return a / b;
    }

    /**
     * Calculates an average value with a fixed scale.
     *
     * @param values values to average (must not be null/empty, and must not contain nulls)
     * @param scale  decimal scale to round to (e.g. 2)
     * @return arithmetic mean of the provided values
     * @throws IllegalArgumentException when {@code values} is empty
     * @throws NullPointerException     when {@code values} or any element is null
     */
    public BigDecimal average(Iterable<BigDecimal> values, int scale) {
        Objects.requireNonNull(values, "values must not be null");

        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (BigDecimal value : values) {
            Objects.requireNonNull(value, "value must not be null");
            sum = sum.add(value);
            count++;
        }

        if (count == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }

        return sum.divide(BigDecimal.valueOf(count), scale, RoundingMode.HALF_UP);
    }
}

