package uz.itpu.ajp.tddStyle;

import org.junit.jupiter.api.Test;
import uz.itpu.ajp.Calculator;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTddImplTest {

    private Calculator calculator = new CalculatorTddImpl();

    @Test
    void testAdd() {
        int a = 10;
        int b = 3;
        int expected = 13;

        int actual = calculator.add(a, b);

        assertEquals(expected, actual);
    }

    @Test
    void testSubtract() {
        int a = 10;
        int b = 3;
        int expected = 7;

        int actual = calculator.subtract(a, b);

        assertEquals(expected, actual);
    }

}