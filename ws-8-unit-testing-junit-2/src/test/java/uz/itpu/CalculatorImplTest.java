package uz.itpu;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
public class CalculatorImplTest {

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before all tests");
    }
    @BeforeEach
    public void beforeEach() {
        System.out.println("Before each...");
    }
    @AfterAll
    public static void afterAll() {
        System.out.println("After all tests");
    }

    private Calculator calculator = new CalculatorImpl();

    @Test
    public void testAdd() {
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testAdd_positive(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testAdd_negative(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 4;
        int actual = calculator.add(1, 2);
        assertNotEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    public void testExample_same(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        Object o1 = new Object();
        Object o2 = o1;
        assertSame(o1, o2);
    }

    @Test
    public void testExample_TrueFalse(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        assertTrue(true);
        assertFalse(false);
    }

    @Test
    public void testExample_Throws(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        assertThrows(RuntimeException.class, () -> throwARuntimeException());
    }

    private void throwARuntimeException() {
        throw new RuntimeException("This is a runtime exception");
    }

    private void notThrowingARuntimeException() {
        System.out.println("This is a normal behaviour");
    }

    @Test
    public void testExample_All(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        assertAll(() -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException());
    }

    @Test
    public void testExample_Assume(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int num = 5;
        assumeTrue(num > 0, "Number should be positive");

        assertAll(() -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException());
    }

    @Test
    public void testExample_notAssume(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int num = 5;
        assumeTrue(num < 0, "Number should be negative");

        assertAll(() -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException());
    }

    @Test
    public void testExample_that(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int num = 5;
        assumingThat(num < 0, () -> assertAll(() -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException(),
                () -> notThrowingARuntimeException()));
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
    @ValueSource(ints = {1, 2, 3, 4, 5})
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
    @CsvSource({"3, 1, 2", "4, 2, 2", "5, 3, 2"})
    public void testSubtractWithCsvSource(int num1, int num2, int num3, Calculator calculator) {
        int expected = num3;
        int actual = calculator.subtract(num1, num2);
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testSubtract.csv", delimiter = '|')
    public void testSubtractWithCsvFileSource(int num1, int num2, Calculator calculator) {
        int expected = 2;
        int actual = calculator.subtract(num1, num2);
        assertEquals(expected, actual);
    }

}