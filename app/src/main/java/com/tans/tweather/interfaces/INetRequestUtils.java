package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

/**
 * Created by tans on 2017/11/19.
 */

public interface INetRequestUtils {

    /**
     * 网络http请求监听
     */
    interface NetRequestListener {
        void onSuccess(Object result);

        void onFail(VolleyError e);
    }

    /**
     * 网络是否可用
     * @return
     */
    boolean isNetWorkAvailable();

    /**
     * 当前位置信息请求
     * @param listener
     */
    void requestLocationInfo(NetRequestListener listener);

    /**
     * 请求大气信息
     * @param location 当前请求城市
     * @param listener
     */
    void requestAtmosphereInfo(String location, NetRequestListener listener);

    /**
     * 当前天气信息
     * @param location
     * @param listener
     */
    void requestConditionInfo(String location, NetRequestListener listener);

    /**
     * 未来10天预报
     * @param location
     * @param listener
     */
    void requestForecastInfo(String location, NetRequestListener listener);

    /**
     * 风
     * @param location
     * @param listener
     */
    void requestWindInfo(String location, NetRequestListener listener);

    void requestCitiesInfo(NetRequestListener listener);
}
