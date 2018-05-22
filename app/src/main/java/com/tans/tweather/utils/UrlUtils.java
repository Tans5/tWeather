package com.tans.tweather.utils;

import java.net.URLEncoder;

/**
 * Created by 鹏程 on 2018/5/22.
 */

public class UrlUtils {
    //风url
    private final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //大气url
    private final static String ATMOSPHERE_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //当前温度
    private final static String CURRENT_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //未来10点温度
    private final static String FUTURE_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //天气类url 后缀
    private final static String WEATHER_URL_TAIL = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    //请求当前位置url
    private final static String LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?location=";
    //请求位置url 的后缀
    private final static String LOCATION_URL_TAIL = "&output=json&ak=xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1";

    //全国城市信息url前缀
    private final static String CHINA_CITIES_URL = "http://www.weather.com.cn/data/list3/city";
    //全国城市信息url后缀
    private final static String CHINA_CITIES_URL_TAIL = ".xml";

    public static String getBaiduLocationUrl(double[] location) {
        String result = LOCATION_URL + location[0] + "," + location[1] + LOCATION_URL_TAIL;
        return URLEncoder.encode(result);
    }

    public static String getWeatherWindUrl(String city) {
        String result = WIND_URL + city + WEATHER_URL_TAIL;

        return URLEncoder.encode(result);
    }

    public static String getWeatherConditionUrl(String city) {
        String result = CURRENT_CONDITION_URL + city + WEATHER_URL_TAIL;

        return URLEncoder.encode(result);
    }

    public static String getWeatherForecastUrl(String city) {
        String result = FUTURE_CONDITION_URL + city + WEATHER_URL_TAIL;

        return URLEncoder.encode(result);
    }

    public static String getWeatherAtomosphereUrl(String city) {
        String result = ATMOSPHERE_URL + city + WEATHER_URL_TAIL;

        return URLEncoder.encode(result);
    }

    public static String getChinaCitiesUrl(String parentCode) {
        String result = CHINA_CITIES_URL + parentCode + CHINA_CITIES_URL_TAIL;

        return URLEncoder.encode(result);
    }
}
