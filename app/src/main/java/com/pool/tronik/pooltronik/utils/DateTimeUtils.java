package com.pool.tronik.pooltronik.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Locale;

public class DateTimeUtils {

    public static String getCurrentDayOfWeek() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime.Property day = localDateTime.dayOfWeek();
        return day.getAsText(Locale.US);
    }

    public static int getCurrentDayOfMonth() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getDayOfMonth();
    }
}
