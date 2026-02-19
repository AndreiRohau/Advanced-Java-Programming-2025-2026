package uz.itpu;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CalculatorImplDependencyInjectionParameterResolverTest.class)
public class CalculatorImplConditionTest {
    @Test
    @EnabledOnOs({OS.MAC})
    public void testAdd_onMac(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    @EnabledOnOs({OS.WINDOWS})
    public void testAdd_onWindows(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
    @EnabledOnOs({OS.LINUX})
    public void testAdd_onLinux(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }

    @Test
//    @EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_17)
    @DisabledOnJre(JRE.JAVA_11)
    public void testAdd_forJreRange(Calculator calculator) { // this calc comes from CalculatorImplDependencyInjectionParameterResolverTest
        int expected = 3;
        int actual = calculator.add(1, 2);
        assertEquals(expected, actual);
        System.out.println("actual: " + actual);
    }
}