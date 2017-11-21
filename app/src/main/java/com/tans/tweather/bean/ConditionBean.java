package com.tans.tweather.bean;

import java.io.Serializable;

/**
 * Created by mine on 2017/11/21.
 */

public class ConditionBean implements Serializable {
    private int code;
    private String date;
    private int temp;
    private String text;

    public ConditionBean() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
