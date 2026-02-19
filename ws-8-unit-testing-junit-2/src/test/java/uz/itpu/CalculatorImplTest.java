package uz.itpu;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
public class CalculatorImplTest {

    @Test
    public void testAdd(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @RepeatedTest(4)
    public void testAdd(RepetitionInfo repetitionInfo) {
        Calculator calculator = new CalculatorImpl();
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
        System.out.println(repetitionInfo);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void testSubtract(int num) {
        Calculator calculator = new CalculatorImpl();
        int expected = 0;
        int actual = calculator.subtract(num, num);
        assertEquals(expected, actual);
    }

    // An example starts
    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"TUESDAY", "THURSDAY"})
    public void testDayOfWeek(DayOfWeek day) {
        assertTrue(day.toString().startsWith("T"));
    }
    // An example ends

    @ParameterizedTest
    @CsvSource({"3, 1", "4, 2", "5, 3"})
    public void testSubtractWithCsvSource(int num1, int num2, Calculator calculator) {
        int expected = 2;
        int actual = calculator.subtract(num1, num2);
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "testSubtract.csv")
    public void testSubtractWithCsvFileSource(String num1, String num2, Calculator calculator) {
        int expected = 2;
        int actual = calculator.subtract(Integer.parseInt(num1), Integer.parseInt(num2));
        assertEquals(expected, actual);

    }

}