package com.tans.tweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    public final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    public final static String ATMOSPHERE_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    public final static String CURRENT_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    public final static String FUTURE_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    public final static String WEATHER_URL_TAIL = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    public final static String LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?location=";
    public final static String LOCATION_URL_TAIL = "&output=json&ak=xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1";
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
        Log.i("URL", url);
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
    public void requestLocationInfo(final INetRequestUtils.NetRequestListener listener) {
        double[] location = getLocation();
        if (location == null) {
            VolleyError e = new VolleyError();
            listener.onFail(e);
            return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                listener.onSuccess(getCurrentCityString(response));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(LOCATION_URL + location[0] + "," + location[1] + LOCATION_URL_TAIL, responseListener, errorListener);
    }

    private double[] getLocation() {
        if (mContext == null)
            return null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String provider = "";
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains((LocationManager.NETWORK_PROVIDER))) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double[] result = {latitude, longitude};
        return result;
    }

    @Override
    public void requestAtmosphereInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getAtmosphereJsonString(response);
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
        requestNet(ATMOSPHERE_URL + java.net.URLEncoder.encode(location) + WEATHER_URL_TAIL, responseListener, errorListener);
    }

    @Override
    public void requestConditionInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getCurrentConditionJsonString(response);
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
        requestNet(CURRENT_CONDITION_URL + java.net.URLEncoder.encode(location) + WEATHER_URL_TAIL, responseListener, errorListener);
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
        requestNet(FUTURE_CONDITION_URL + java.net.URLEncoder.encode(location) + WEATHER_URL_TAIL, responseListener, errorListener);
    }

    @Override
    public void requestWindInfo(String location, final NetRequestListener listener) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String resultString = getWindJsonString(response);
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
        requestNet(WIND_URL + java.net.URLEncoder.encode(location) + WEATHER_URL_TAIL, responseListener, errorListener);
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

    private String getCurrentCityString(String result) {
        String city = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("result");
            jsonObject = (JSONObject) jsonObject.get("addressComponent");
            city = jsonObject.get("city").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return city;
    }
}
