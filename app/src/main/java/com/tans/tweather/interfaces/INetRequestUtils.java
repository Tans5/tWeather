package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

/**
 * Created by tans on 2017/11/19.
 */

public interface INetRequestUtils {

    interface NetRequestListener {
        void onSuccess(Object result);

        void onFail(VolleyError e);
    }

    boolean isNetWorkAvailable();

    void requestLocationInfo(NetRequestListener listener);

    void requestAtmosphereInfo(String location, NetRequestListener listener);

    void requestConditionInfo(String location, NetRequestListener listener);

    void requestForecastInfo(String location, NetRequestListener listener);

    void requestWindInfo(String location, NetRequestListener listener);

    void requestCitiesInfo(NetRequestListener listener);
}
