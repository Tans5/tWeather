package com.tans.tweather.utils;

import com.tans.tweather.interfaces.INetRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tans on 2017/11/19.
 */

public class NetRequestUtils implements INetRequestUtils {

    private static NetRequestUtils instance = null;

    public final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String ATMOSPHERE_URL="https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String CURRENT_CONDITION_URL="https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String FUTURE_CONDITION_URL="https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    public static NetRequestUtils newInstance() {
        if (instance == null) {
            instance = new NetRequestUtils();
        }
        return instance;
    }

    private NetRequestUtils() {

    }

    @Override
    public boolean isNetWorkAvailable() {
        return false;
    }

    @Override
    public void requestLocationInfo() {

    }

    @Override
    public void requestLocationInfo(NetRequestListener listener) {

    }

    @Override
    public void requestWeatherInfo(NetRequestListener listener) {

    }

    @Override
    public void requestCitiesInfo(NetRequestListener listener) {

    }



    private String getAtmosphereJsonString(String result)
    {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("atmosphere");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private String getWindJsonString(String result)
    {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("wind");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private String getCurrentConditionJsonString(String result)
    {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("item");
            jsonObject = (JSONObject) jsonObject.get("condition");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private String getFutureConditionJsonString(String result)
    {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
