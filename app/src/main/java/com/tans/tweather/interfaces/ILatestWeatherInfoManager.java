package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by tans on 2017/11/19.
 */

public interface ILatestWeatherInfoManager {
    public static interface LatestWeatherUpdateListener {
        public void onSuccess();

        public void onFail(VolleyError e);
    }

    public void updateLatestWeatherInfo(LatestWeatherUpdateListener listener);

    public void updateLatestWeatherInfo();

}
