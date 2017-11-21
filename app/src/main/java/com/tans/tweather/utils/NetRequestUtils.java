package com.tans.tweather.utils;

import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.interfaces.INetRequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tans on 2017/11/19.
 */

public class NetRequestUtils implements INetRequestUtils {

    private static NetRequestUtils instance = null;

    public final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String ATMOSPHERE_URL="https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String CURRENT_CONDITION_URL="https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String FUTURE_CONDITION_URL="https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    private static OkHttpClient httpClient = new OkHttpClient();

    public static NetRequestUtils newInstance() {
        if (instance == null) {
            instance = new NetRequestUtils();
        }
        return instance;
    }

    private NetRequestUtils() {

    }

    private void requestInternet(String url,Callback callback)
    {
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        call.enqueue(callback);
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

    @Override
    public void requestAtmosphere(final NetRequestListener listener) {
        Callback callback =new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                listener.onSuccess(gson.fromJson(getAtmosphereJsonString(response.body().string()), AtmosphereBean.class));
            }
        };
        requestInternet(ATMOSPHERE_URL,callback);
    }

    @Override
    public void requestCondition(final NetRequestListener listener) {
        Callback callback =new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                listener.onSuccess(gson.fromJson(getCurrentConditionJsonString(response.body().string()), ConditionBean.class));
            }
        };
        requestInternet(CURRENT_CONDITION_URL,callback);
    }

    @Override
    public void requestForecast(final NetRequestListener listener) {

        Callback callback =new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ArrayList<ForecastBean> result = gson.fromJson(getFutureConditionJsonString(response.body().string()),new TypeToken<ArrayList<ForecastBean>>(){}.getType());
                listener.onSuccess(result);
                System.out.println(result.get(0).getItem().getForecast().getText());
            }
        };
        requestInternet(FUTURE_CONDITION_URL,callback);
    }

    @Override
    public void requestWind(NetRequestListener listener) {

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
            JSONArray jsonObjects =  (JSONArray) jsonObject.get("channel");
            resultString = jsonObjects.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
