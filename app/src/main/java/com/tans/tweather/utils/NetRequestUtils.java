package com.tans.tweather.utils;

import com.tans.tweather.interfaces.INetRequestUtils;

/**
 * Created by tans on 2017/11/19.
 */

public class NetRequestUtils implements INetRequestUtils {

    private static NetRequestUtils instance = null;

    public static NetRequestUtils newInstance() {
        if (instance == null) {
            instance = new NetRequestUtils();
        }
        return instance;
    }

    private NetRequestUtils() {

    }

    @Override
    public boolean isNetWorkAvailable() {
        return false;
    }

    @Override
    public void requestLocationInfo() {

    }

    @Override
    public void requestLocationInfo(NetRequestListener listener) {

    }

    @Override
    public void requestWeatherInfo(NetRequestListener listener) {

    }

    @Override
    public void requestCitiesInfo(NetRequestListener listener) {

    }
}
