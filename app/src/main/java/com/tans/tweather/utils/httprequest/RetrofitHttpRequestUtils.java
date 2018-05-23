package com.tans.tweather.utils.httprequest;

import android.content.Context;

import com.tans.tweather.application.BaseApplication;

/**
 * Created by 鹏程 on 2018/5/23.
 */

public class RetrofitHttpRequestUtils extends BaseHttpRequestUtils {

    private static RetrofitHttpRequestUtils instance;

    public static RetrofitHttpRequestUtils newInstance() {
        if(instance == null) {
            instance = new RetrofitHttpRequestUtils();
            instance.init(BaseApplication.getInstance());
        }
        return  instance;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    public void request(String url, HttpRequestMethod method, Object requestParams, HttpRequestListener listener) {

    }

    private RetrofitHttpRequestUtils() {

    }

}
