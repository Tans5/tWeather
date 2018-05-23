package com.tans.tweather.utils;

import java.net.URLEncoder;

/**
 * Created by 鹏程 on 2018/5/22.
 */

public class UrlUtils {
    private final static String WEATHER_URL = "https://query.yahooapis.com/v1/public/yql";
    private final static String BAIDU_LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/";
    //全国城市信息url前缀
    private final static String CHINA_CITIES_URL = "http://www.weather.com.cn/data/list3/city";
    //全国城市信息url后缀
    private final static String CHINA_CITIES_URL_TAIL = ".xml";
    //请求当前位置url
    private final static String LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?location=";
    //请求位置url 的后缀
    private final static String LOCATION_URL_TAIL = "&output=json&ak=xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1";

    public static String getBaiduLocationUrl(double[] location) {
        StringBuilder p = new StringBuilder();
        p.append("location=")
                .append(location[0])
                .append(",")
                .append(location[1])
                .append("&")
                .append("output=json")
                .append("&")
                .append("ak=xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1");
        StringBuilder result = new StringBuilder();
        result.append(BAIDU_LOCATION_URL)
                .append("?")
                .append(p.toString());
        return  result.toString();
    }

    public static String getWeatherWindUrl(String city) {
        StringBuilder yql = new StringBuilder();
        yql.append("select wind from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"")
                .append(city)
                .append("\")");
        StringBuilder p = new StringBuilder();
        p.append("q=")
                .append(URLEncoder.encode(yql.toString()))
                .append("&")
                .append("format=json")
                .append("&")
                .append("env=store://datatables.org/alltableswithkeys");
        StringBuilder result = new StringBuilder();
        result.append(WEATHER_URL)
                .append("?")
                .append(p.toString());
        return result.toString();
    }

    public static String getWeatherConditionUrl(String city) {
        StringBuilder yql = new StringBuilder();
        yql.append("select item.condition from weather.forecast where u=\"c\" and  woeid in (select woeid from geo.places(1) where text=\"")
                .append(city)
                .append("\")");
        StringBuilder p = new StringBuilder();
        p.append("q=")
                .append(URLEncoder.encode(yql.toString()))
                .append("&")
                .append("format=json")
                .append("&")
                .append("env=store://datatables.org/alltableswithkeys");
        StringBuilder result = new StringBuilder();
        result.append(WEATHER_URL)
                .append("?")
                .append(p.toString());
        return result.toString();
    }

    public static String getWeatherForecastUrl(String city) {
        StringBuilder yql = new StringBuilder();
        yql.append("select item.forecast from weather.forecast where u=\"c\" and  woeid in (select woeid from geo.places(1) where text=\"")
                .append(city)
                .append("\")");
        StringBuilder p = new StringBuilder();
        p.append("q=")
                .append(URLEncoder.encode(yql.toString()))
                .append("&")
                .append("format=json")
                .append("&")
                .append("env=store://datatables.org/alltableswithkeys");
        StringBuilder result = new StringBuilder();
        result.append(WEATHER_URL)
                .append("?")
                .append(p.toString());
        return result.toString();
    }

    public static String getWeatherAtomosphereUrl(String city) {
        StringBuilder yql = new StringBuilder();
        yql.append("select atmosphere from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"")
                .append(city)
                .append("\")");
        StringBuilder p = new StringBuilder();
        p.append("q=")
                .append(URLEncoder.encode(yql.toString()))
                .append("&")
                .append("format=json")
                .append("&")
                .append("env=store://datatables.org/alltableswithkeys");
        StringBuilder result = new StringBuilder();
        result.append(WEATHER_URL)
                .append("?")
                .append(p.toString());
        return result.toString();
    }

    public static String getChinaCitiesUrl(String parentCode) {
        String result = CHINA_CITIES_URL + parentCode + CHINA_CITIES_URL_TAIL;

        return result;
    }
}
