package com.tans.tweather.interfaces;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

/**
 * Created by tans on 2017/11/19.
 */

public interface INetRequestUtils {

    public static interface NetRequestListener {
        public void onSuccess(Object result);

        public void onFail(VolleyError e);
    }

    public boolean isNetWorkAvailable();

    public void requestLocationInfo();

    public void requestAtmosphereInfo(String location, NetRequestListener listener);

    public void requestConditionInfo(String location, NetRequestListener listener);

    public void requestForecastInfo(String location, NetRequestListener listener);

    public void requestWindInfo(String location, NetRequestListener listener);

    public void requestCitiesInfo(NetRequestListener listener);
}
