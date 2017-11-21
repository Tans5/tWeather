package com.tans.tweather.interfaces;

import java.io.IOException;

/**
 * Created by tans on 2017/11/19.
 */

public interface INetRequestUtils {

    public static interface NetRequestListener
    {
        public void onSuccess(Object result);
        public void onFail(IOException e);
    }
    public boolean isNetWorkAvailable();
    public void requestLocationInfo();
    public void requestLocationInfo(NetRequestListener listener);
    public void requestWeatherInfo(NetRequestListener listener);
    public void requestCitiesInfo(NetRequestListener listener);
    public void requestAtmosphere(NetRequestListener listener);
    public void requestCondition(NetRequestListener listener);
    public void requestForecast(NetRequestListener listener);
    public void requestWind(NetRequestListener listener);
}
