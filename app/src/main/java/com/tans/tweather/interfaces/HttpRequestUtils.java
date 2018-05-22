package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by tans on 2017/11/19.
 */

public interface HttpRequestUtils {

    /**
     * 网络http请求监听
     */
    interface NetRequestListener {
        void onSuccess(Object result);
        void onFail(String e);
    }

    interface HttpRequestListener<T> {
        void onSuccess(T result);
        void onFail(String e);
    }

    enum HttpRequestMethod {
        GET,
        POST
    }

    void init();
    void request(String url,HttpRequestMethod method,Object requestParams,
                 HttpRequestListener listener);
}
