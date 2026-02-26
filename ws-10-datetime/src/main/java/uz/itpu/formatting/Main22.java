package uz.itpu.formatting;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

public class Main22 {
    public static void main(String[] args) {
        periodExample();
        durationExample();
    }

    public static void periodExample() {
        Period p1 = Period.of(1, 1, 1);
        System.out.println(p1);

        Period p2 = Period.ofDays(100);
        System.out.println(p2);

        LocalDate ld1 = LocalDate.of(2022, 1, 1);
        LocalDate ld2 = LocalDate.now();
        Period p3 = Period.between(ld1, ld2);
        System.out.println(p3);

        System.out.println(p3.getDays());

        long nrOfDaysBetweenTheDates = ChronoUnit.DAYS.between(ld1, ld2);
        System.out.println(nrOfDaysBetweenTheDates);
    }

    public static void durationExample() {
        Duration d1 = Duration.of(1, ChronoUnit.DAYS);
        System.out.println(d1);

        Duration d2 = Duration.ofHours(5);
        System.out.println(d2);

        LocalTime lt1 = LocalTime.now();
        LocalTime lt2 = LocalTime.of(15, 0);
        Duration d3 = Duration.between(lt1, lt2);
        System.out.println(d3);
    }
}
