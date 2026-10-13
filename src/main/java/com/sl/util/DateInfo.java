package com.sl.util;
public class DateInfo{
    private int week;
    private int day;
    private String date;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "\nDateInfo{" +
                "week=" + week +
                ", day=" + day +
                ", date='" + date + '\'' +
                '}';
    }
}