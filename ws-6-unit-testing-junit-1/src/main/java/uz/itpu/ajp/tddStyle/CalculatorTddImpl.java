package uz.itpu.ajp.tddStyle;

import uz.itpu.ajp.Calculator;

import java.math.BigDecimal;

public class CalculatorTddImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }

    @Override
    public int multiply(int a, int b) {
        return 0;
    }

    @Override
    public int divide(int a, int b) {
        return 0;
    }

    @Override
    public BigDecimal average(Iterable<BigDecimal> values, int i) {
        return null;
    }
}
