package uz.itpu.timeline;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class Main2 {
    public static void main(String[] args) {
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
//            System.out.println(zoneId);
            ZoneId zoneIdObj = ZoneId.of(zoneId);
            String zoneIdDisplayname = zoneIdObj.getDisplayName(TextStyle.FULL, Locale.US);
            System.out.println(zoneId + " : " + zoneIdDisplayname);
        }


        ZoneId zoneId = ZoneId.of("US/Pacific");
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt1 = ZonedDateTime.of(ldt, zoneId);
        System.out.println(ldt);
        System.out.println(zdt1);

        ZonedDateTime zdt2 = ldt.atZone(zoneId);
        ZonedDateTime zdt3 = ZonedDateTime.now();
        System.out.println(zdt3.getZone());

        ZonedDateTime zonedDateTime = ldt.atZone(zoneId);
        System.out.println(zonedDateTime);

        zonedDateTimeExample();
    }

    public static void zonedDateTimeExample() {
        ZonedDateTime now = ZonedDateTime.now();

        System.out.println("\nzonedDateTimeExample()");
        System.out.println(now);
        System.out.println(now.withZoneSameInstant(ZoneId.of("US/Pacific")));
    }
}