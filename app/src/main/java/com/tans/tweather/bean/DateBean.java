package com.tans.tweather.bean;

import java.io.Serializable;

/**
 * Created by mine on 2017/11/21.
 */

public class DateBean implements Serializable {

    private int day;
    private int month;
    private int year;

    public DateBean(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean equals(DateBean data) {
        if (data!=null&&this.day == data.day && this.year == data.year && this.year == data.year)
            return true;
        else
            return false;
    }
}
