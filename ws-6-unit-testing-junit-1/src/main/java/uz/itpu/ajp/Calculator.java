package uz.itpu.ajp;

import java.math.BigDecimal;

public interface Calculator {
    int add(int a, int b);

    int subtract(int a, int b);

    int multiply(int a, int b);

    int divide(int a, int b);

    BigDecimal average(Iterable<BigDecimal> values, int i);
}
