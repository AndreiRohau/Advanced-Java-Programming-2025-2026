package uz.itpu.timeline;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class Main3 {
    public static void main(String[] args) {

        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock);
        System.out.println();

        Instant instant = clock.instant();
        System.out.println(instant);
        System.out.println();

        Clock clock1 = Clock.systemUTC();
        System.out.println(clock1.instant());
        System.out.println();

        Clock clock2 = Clock.offset(clock, Duration.ofHours(10));
        System.out.println(clock2.instant());

    }
}