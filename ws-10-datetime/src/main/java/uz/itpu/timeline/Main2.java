package uz.itpu.timeline;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

public class Main2 {
    public static void main(String[] args) {
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            ZoneId zoneIdObj = ZoneId.of(zoneId);
            String zoneIdDisplayname = zoneIdObj.getDisplayName(TextStyle.FULL, Locale.US);
            System.out.println(zoneId + " : " + zoneIdDisplayname);
        }


//        ZoneId zoneId = ZoneId.of("US/Pacific");
//        LocalDateTime ldt = LocalDateTime.now();
//        ZonedDateTime zdt1 = ZonedDateTime.of(ldt, zoneId);
//        System.out.println(zdt1);
//
//        ZonedDateTime zdt2 = ldt.atZone(zoneId);
//        ZonedDateTime zdt3 = ZonedDateTime.now();
//        System.out.println(zdt3.getZone());
    }
}