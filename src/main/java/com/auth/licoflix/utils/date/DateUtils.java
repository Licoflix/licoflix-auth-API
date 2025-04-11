package com.auth.licoflix.utils.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {
    public static String formatLocalDateTimeToString(LocalDateTime dateTime, String timeZone) {
        String formattedDate;
        DateTimeFormatter formatter;
        String countryCode = ZoneId.of(timeZone).getId().split("/")[1];

        if (countryCode.equals("Sao_Paulo")) {
            formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", new Locale("pt", "BR"));
            formattedDate = dateTime.format(formatter);
            int position = formattedDate.indexOf("de");
            if (position != -1 && position + 3 < formattedDate.length()) {
                char next = Character.toUpperCase(formattedDate.charAt(position + 3));
                formattedDate = formattedDate.substring(0, position + 3) + next + formattedDate.substring(position + 4);
            }
        } else {
            Locale locale = Locale.forLanguageTag(ZoneId.of(timeZone).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", locale);
            formattedDate = dateTime.format(formatter);
        }
        return formattedDate;
    }

    public static String formatLocalDateTimeToBasicFormatString(LocalDateTime dateTime, String timeZone) {
        String formattedDate = "";
        String countryCode = ZoneId.of(timeZone).getId().split("/")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (!countryCode.equals("Sao_Paulo")) {
            Locale locale = Locale.forLanguageTag(ZoneId.of(timeZone).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", locale);
        }

        formattedDate = dateTime.format(formatter);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);

        return formattedDate;
    }
}