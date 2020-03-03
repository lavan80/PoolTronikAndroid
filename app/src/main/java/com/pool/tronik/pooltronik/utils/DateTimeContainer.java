package com.pool.tronik.pooltronik.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DateTimeContainer {
    private int year;
    private int month;
    private int dayOfWeek;
    private int dayOfMonth;
    private int hour = -1;
    private int minutes = -1;
    private long millis;
    private List<Integer> repeatList;

    public DateTimeContainer() {
        repeatList = new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public void addRepeat(int day){
        repeatList.add(day);
    }

    public void removeDay(int day) {
        int index = repeatList.indexOf(day);
        if (index >=0)
            repeatList.remove(index);
    }

    public List<Integer> getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(List<Integer> repeatList) {
        this.repeatList = repeatList;
    }
}
