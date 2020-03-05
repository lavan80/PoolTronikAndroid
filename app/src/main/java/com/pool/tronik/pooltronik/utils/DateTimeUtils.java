package com.pool.tronik.pooltronik.utils;

import android.content.Context;

import com.pool.tronik.pooltronik.R;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {


    public static List<String> makeNextDatesFromDateAndRepetition(LocalDateTime startDate, List<Integer> repetitionList) {
        List<String> nextDateList = new ArrayList<>();
        int startDay = startDate.getDayOfWeek();
        for (Integer day : repetitionList) {
            if (startDay > day) {
                int step = (7 - startDay) + day;
                String date = startDate.plusDays(step).toString();
                nextDateList.add(date);
            }
            else if (startDay < day) {
                int step = day - startDay ;
                String date = startDate.plusDays(step).toString();
                nextDateList.add(date);
            }
            else {
                String date = startDate.plusDays(7).toString();
                nextDateList.add(date);
            }
        }
        return nextDateList;
    }

    public static LocalDateTime getLocalDateTime(int year, int month, int day, int hour, int minute) {
        LocalDateTime localDateTime = new LocalDateTime( year, month, day, hour, minute);
        return localDateTime;
    }

    public static LocalDateTime getLocalDateTime(int year, int month, int day) {
        LocalDateTime localDateTime = new LocalDateTime( year,  month,  day,0,0);
        return localDateTime;
    }

    public static LocalDateTime getLocalDateTime(DateTimeContainer dateTimeContainer) {
        LocalDateTime localDateTime = new LocalDateTime( dateTimeContainer.getYear(),  dateTimeContainer.getMonth(),
                dateTimeContainer.getDayOfMonth(),  dateTimeContainer.getHour(),  dateTimeContainer.getMinutes());
        return localDateTime;
    }

    public static String getDayOfWeek(List<Integer> days, Context context) {
        String [] week = context.getResources().getStringArray(R.array.weeks);
        Collections.sort(days);
        String str = "";
        for (Integer day : days) {
            try {
                str += week[day-1] + ", ";
            } catch (Exception e) {

            }
        }
        int index = str.lastIndexOf(",");
        if (index >= 0) {
            str = str.substring(0, index);
        }
        return str;
    }
    public static String getDayOfWeek(int day, Context context) {
        String [] week = context.getResources().getStringArray(R.array.weeks);
        String str = "";
        try {
            str = week[day-1];
        }catch (Exception e){}
        return str;
    }
    public static String getCurrentDayOfWeek() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime.Property day = localDateTime.dayOfWeek();
        return day.getAsText(Locale.US);
    }
    public static int getCurrentNumberDayOfWeek() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getDayOfWeek();
    }

    public static int getCurrentDayOfMonth() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getDayOfMonth();
    }

    public static int getCurrentYear() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getYear();
    }

    public static int getCurrentMonth() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getMonthOfYear();
    }

    public static LocalDateTime createLocalDateTime(String date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        //DateTime customDateTimeFromString = new DateTime("2018-05-05T10:11:12.123");
        return localDateTime;
    }
}
