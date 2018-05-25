package com.tans.tweather.utils;

import com.tans.tweather.bean.request.HttpGetParams;

/**
 * Created by 鹏程 on 2018/5/22.
 */

public class UrlUtils {

    public static String getWeatherBaseUrl() {
        return "https://query.yahooapis.com/";
    }

    public static String getWeatherPath() {
        return "v1/public/yql/";
    }

    public static HttpGetParams createWindParams(String city) {
        HttpGetParams httpGetParams = createWeatherBaseParams();
        httpGetParams.add("q","select wind from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""
                + city + "\")");
        return httpGetParams;
    }

    public static HttpGetParams createConditionParams(String city) {
        HttpGetParams httpGetParams = createWeatherBaseParams();
        httpGetParams.add("q","select item.condition from weather.forecast where u=\"c\" and  woeid in (select woeid from geo.places(1) where text=\""
                + city + "\")");
        return httpGetParams;
    }

    public static HttpGetParams createAtmosphereParams(String city) {
        HttpGetParams httpGetParams = createWeatherBaseParams();
        httpGetParams.add("q","select atmosphere from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""
                + city + "\")");
        return httpGetParams;
    }

    public static HttpGetParams createForecastParams(String city) {
        HttpGetParams httpGetParams = createWeatherBaseParams();
        httpGetParams.add("q","select item.forecast from weather.forecast where u=\"c\" and  woeid in (select woeid from geo.places(1) where text=\""
                + city + "\")");
        return httpGetParams;
    }

    public static String getBaiduLocationBaseUrl() {
        return "http://api.map.baidu.com/";
    }

    public static String getBaiduLocationPath() {
        return "geocoder/v2/";
    }

    public static HttpGetParams createBaiduLocationParams(double[] location) {
        HttpGetParams params = new HttpGetParams();
        params.add("location", location[0] + "," +location[1])
                .add("output", "json")
                .add("ak", "xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1");
        return params;
    }

    public static String getChinaCitiesBaseUrl() {
        return "http://www.weather.com.cn/";
    }

    public static String getChinaCitiesPath(String parentCode) {
        return "data/list3/city" + parentCode + ".xml";
    }


    private static HttpGetParams createWeatherBaseParams() {
        HttpGetParams params = new HttpGetParams();
        params.add("format", "json")
                .add("env", "store://datatables.org/alltableswithkeys");
        return params;
    }
}
