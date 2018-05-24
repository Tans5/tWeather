package com.tans.tweather.utils.httprequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
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
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.weather.AtmosphereBean;
import com.tans.tweather.bean.weather.ConditionBean;
import com.tans.tweather.bean.weather.ForecastBean;
import com.tans.tweather.bean.weather.WindBean;
import com.tans.tweather.utils.ResponseConvertUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 鹏程 on 2018/5/22.
 */

public class VolleyHttpRequestUtils extends BaseHttpRequestUtils {
    private static VolleyHttpRequestUtils instance;

    private RequestQueue mQueue;

    private class U8StringRequest extends StringRequest {
        public U8StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url,listener,errorListener);
        }

        public U8StringRequest(int method,String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method,url,listener,errorListener);
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

    public static VolleyHttpRequestUtils newInstance() {
        if(instance == null) {
            instance = new VolleyHttpRequestUtils();
            instance.init(BaseApplication.getInstance());
        }
        return instance;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        if(mQueue == null) {
            mQueue = Volley.newRequestQueue(BaseApplication.getInstance().getBaseContext());
        }
    }

    @Override
    public void request(String url, HttpRequestMethod method, final Object requestParams, final HttpRequestListener listener) {
        Response.Listener<String> vListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Class c = listener.getResultType();
                listener.onSuccess(convertResponse(response,c));
            }
        };
        Response.ErrorListener vErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFail(error.getMessage());
            }
        };
        Request request;
        if(method == HttpRequestMethod.GET || requestParams == null) {
            request = new U8StringRequest(url,vListener,vErrorListener);
        } else {
            request = new U8StringRequest(Request.Method.POST,url,vListener,vErrorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Gson gson = new Gson();
                    String s = gson.toJson(requestParams);
                    return s.getBytes();
                }
            };
        }
        mQueue.add(request);
    }

    private VolleyHttpRequestUtils() {

    }

    private Object convertResponse(String response,Class c) {
        String resultString ;
        Gson gson = new Gson();
        Object result = null;
        if(c == AtmosphereBean.class) {
            resultString = ResponseConvertUtils.getAtmosphereJsonString(response);
            result = gson.fromJson(resultString,c);
        } else if(c == ConditionBean.class) {
            resultString = ResponseConvertUtils.getCurrentConditionJsonString(response);
            result = gson.fromJson(resultString,c);
        } else if(c == List.class) {
            resultString = ResponseConvertUtils.getFutureConditionJsonString(response);
            result = gson.fromJson(resultString, new TypeToken<ArrayList<ForecastBean>>() {
            }.getType());
        } else if(c == WindBean.class) {
            resultString = ResponseConvertUtils.getWindJsonString(response);
            result = gson.fromJson(resultString,c);
        } else if(c == String.class) {
            result = response;
        }
        return result;
    }

}
