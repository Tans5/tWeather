package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by tans on 2017/11/19.
 */

public interface ILatestWeatherInfoManager {

    /**
     * 天气更新的监听
     */
    interface LatestWeatherUpdateListener {
        void onSuccess();
        void onFail(VolleyError e);
    }


    /**
     * 请求更新天气 主要在activity中调用
     * @param listener
     */
    void updateLatestWeatherInfo(LatestWeatherUpdateListener listener);

    /**
     * 请求天气更新，主要在广播中调用
     */
    void updateLatestWeatherInfo();

}
