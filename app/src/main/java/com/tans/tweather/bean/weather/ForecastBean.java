package com.tans.tweather.bean.weather;

import java.io.Serializable;

/**
 * Created by mine on 2017/11/21.
 */

public class ForecastBean implements Serializable {

    private Item item;

    public ForecastBean() {

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public static class Item implements Serializable
    {
        private InnerForecastBean forecast;

        public Item() {
        }

        public InnerForecastBean getForecast() {
            return forecast;
        }

        public void setForecast(InnerForecastBean forecast) {
            this.forecast = forecast;
        }
    }

    public static class InnerForecastBean implements Serializable {
        private int code;
        private String date;
        private String day;
        private int high;
        private int low;
        private String text;

        public InnerForecastBean() {
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

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getHigh() {
            return high;
        }

        public void setHigh(int high) {
            this.high = high;
        }

        public int getLow() {
            return low;
        }

        public void setLow(int low) {
            this.low = low;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
