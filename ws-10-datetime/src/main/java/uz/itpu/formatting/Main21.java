package uz.itpu.formatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Main21 {
    public static void main(String[] args) {
        LocalDateTime ldt = LocalDateTime.now();
        String formattedDate = DateTimeFormatter.BASIC_ISO_DATE.format(ldt);
        System.out.println(ldt);
        System.out.println(formattedDate);
        System.out.println();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        System.out.println(dtf.format(ldt));
        System.out.println();

        String formatStyleDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(ldt);
        System.out.println(formatStyleDate);
        System.out.println();
    }
}
