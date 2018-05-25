package com.tans.tweather.utils.httprequest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by 鹏程 on 2018/5/23.
 */

public abstract class BaseHttpRequestUtils {

    Context mContext;

    public interface HttpRequestListener<T> {
        void onSuccess(T result);
        Class<T> getResultType();
        void onFail(String e);
    }

    public enum HttpRequestMethod {
        GET,
        POST
    }

    protected void init(Context context) {
        mContext = context;
    }

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

    abstract public void request(String baseUrl, String path, HttpRequestMethod method, Object requestParams, HttpRequestListener listener);

}
