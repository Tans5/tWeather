package com.tans.tweather.interfaces;

/**
 * Created by tans on 2017/11/19.
 */

public interface INetRequestUtils {

    public static interface NetRequestListener
    {
        public void onSeccess();
        public void OnFail();
    }
    public boolean isNetWorkAvailable();
    public void requestLocationInfo();
    public void requestLocationInfo(NetRequestListener listener);
    public void requestWeatherInfo(NetRequestListener listener);
    public void requestCitiesInfo(NetRequestListener listener);
}
