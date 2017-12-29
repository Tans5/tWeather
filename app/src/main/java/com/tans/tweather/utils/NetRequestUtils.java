package com.tans.tweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tans on 2017/11/19.
 */

public class NetRequestUtils implements INetRequestUtils {

    private static NetRequestUtils instance = null;

    //风url
    public final static String WIND_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //大气url
    public final static String ATMOSPHERE_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20atmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //当前温度
    public final static String CURRENT_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //未来10点温度
    public final static String FUTURE_CONDITION_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20item.forecast%20from%20weather.forecast%20where%20u%3D%22c%22%20and%20%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    //天气类url 后缀
    public final static String WEATHER_URL_TAIL = "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    //请求当前位置url
    public final static String LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?location=";
    //请求位置url 的后缀
    public final static String LOCATION_URL_TAIL = "&output=json&ak=xq4Ftq8nOeLbtCAwo8PYnetOY1QlFyX1";
    //全国城市信息url前缀
    public final static String CHINA_CITIES_URL = "http://www.weather.com.cn/data/list3/city";
    //全国城市信息url后缀
    public final static String CHINA_CITIES_URL_TAIL = ".xml";

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

    /**
     * http请求
     *
     * @param url      地址
     * @param response 成功监听
     * @param error    错误监听
     */
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
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
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

    /**
     * 获取当前位置，需要用户开启 权限
     *
     * @return 返回精度和纬度的double数组，空的话就是没有权限。
     */
    private double[] getLocation() {
        if (mContext == null)
            return null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String provider = "";
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains((LocationManager.GPS_PROVIDER))) {
            provider = LocationManager.GPS_PROVIDER;
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
        if (location == null)
            return null;
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
    public void requestCitiesInfo(final NetRequestListener listener, String parentCityCode) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error);
            }
        };
        requestNet(CHINA_CITIES_URL + parentCityCode + CHINA_CITIES_URL_TAIL, responseListener, errorListener);
    }

    /**
     * @param result 网络请求返回的json 字符串
     * @return 描述大气的json字符串
     */
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

    /**
     * @param result
     * @return 描述风的json字符串
     */
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

    /**
     * @param result
     * @return 描述当前天气的json字符串
     */
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

    /**
     * @param result
     * @return 未来10天天气的字符串
     */
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

    /**
     * @param result
     * @return 返回当前位置的城市
     */
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

    private class StringRequest extends Request<String> {
        private Response.Listener<String> mListener;

        /**
         * Creates a new request with the given method.
         *
         * @param method        the request {@link Method} to use
         * @param url           URL to fetch the string at
         * @param listener      Listener to receive the String response
         * @param errorListener Error listener, or null to ignore errors
         */
        public StringRequest(int method, String url, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            mListener = listener;
        }

        /**
         * Creates a new GET request.
         *
         * @param url           URL to fetch the string at
         * @param listener      Listener to receive the String response
         * @param errorListener Error listener, or null to ignore errors
         */
        public StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        @Override
        protected void onFinish() {
            super.onFinish();
            mListener = null;
        }

        @Override
        protected void deliverResponse(String response) {
            if (mListener != null) {
                mListener.onResponse(response);
            }
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String parsed;
            try {
                parsed = new String(response.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

}
