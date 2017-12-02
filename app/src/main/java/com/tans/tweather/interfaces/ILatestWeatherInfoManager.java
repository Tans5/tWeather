package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by tans on 2017/11/19.
 */

public interface ILatestWeatherInfoManager {
    interface LatestWeatherUpdateListener {
        void onSuccess();

        void onFail(VolleyError e);
    }

    interface LoadCurrentCityListener {
        void onSuccess();

        void onFail(VolleyError e);
    }

    void updateLatestWeatherInfo(LatestWeatherUpdateListener listener);

    void updateLatestWeatherInfo();

}
