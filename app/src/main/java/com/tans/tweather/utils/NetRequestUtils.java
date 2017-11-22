package com.tans.tweather.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tans.tweather.MainActivity;
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.bean.WindBean;
import com.tans.tweather.interfaces.INetRequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tans on 2017/11/19.
 */

public class NetRequestUtils implements INetRequestUtils {

    private static NetRequestUtils instance = null;

    public final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String ATMOSPHERE_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String CURRENT_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String FUTURE_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%88%90%E9%83%BD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private Context mContext = null;
    private RequestQueue mQueue = null;

    public static NetRequestUtils newInstance() {
        if (instance == null) {
            instance = new NetRequestUtils();
        }
        return instance;
    }

    private NetRequestUtils() {

    }

    private void requestNet(String url, Response.Listener<String> response, Response.ErrorListener error) {
        StringRequest request = new StringRequest(url, response, error);
        mQueue.add(request);
    }

    public void setContext(Context context) {
        this.mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public boolean isNetWorkAvailable() {
        return false;
    }

    @Override
    public void requestLocationInfo() {

    }

    @Override
    public void requestAtmosphereInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getFutureConditionJsonString(response);
                Gson gson = new Gson();
                AtmosphereBean result = gson.fromJson(resultString, AtmosphereBean.class);
                listener.onSuccess(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(ATMOSPHERE_URL, responseListener, errorListener);
    }

    @Override
    public void requestConditionInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getFutureConditionJsonString(response);
                Gson gson = new Gson();
                ConditionBean result = gson.fromJson(resultString, ConditionBean.class);
                listener.onSuccess(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(CURRENT_CONDITION_URL, responseListener, errorListener);
    }

    @Override
    public void requestForecastInfo(String location, final NetRequestListener listener) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getFutureConditionJsonString(response);
                Gson gson = new Gson();
                List<ForecastBean> result = gson.fromJson(resultString, new TypeToken<ArrayList<ForecastBean>>() {
                }.getType());
                listener.onSuccess(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(FUTURE_CONDITION_URL, responseListener, errorListener);
    }

    @Override
    public void requestWindInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getFutureConditionJsonString(response);
                Gson gson = new Gson();
                WindBean result = gson.fromJson(resultString, WindBean.class);
                listener.onSuccess(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(WIND_URL, responseListener, errorListener);
    }


    @Override
    public void requestCitiesInfo(NetRequestListener listener) {

    }


    private String getAtmosphereJsonString(String result) {
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

    private String getWindJsonString(String result) {
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

    private String getCurrentConditionJsonString(String result) {
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

    private String getFutureConditionJsonString(String result) {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            JSONArray jsonArray = (JSONArray) jsonObject.get("channel");
            resultString = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
