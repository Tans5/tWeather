package com.tans.tweather.manager;

import android.util.Log;

import com.android.volley.VolleyError;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by mine on 2017/12/28.
 */

public class ChinaCitiesManager {

    public static String TAG = ChinaCitiesManager.class.getSimpleName();

    public static void CityTest() {
        NetRequestUtils netRequestUtils = NetRequestUtils.newInstance();
        netRequestUtils.setContext(BaseApplication.getInstance());
        netRequestUtils.requestCitiesInfo(new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                String s = result.toString();
                Log.i(TAG,s);
            }

            @Override
            public void onFail(VolleyError e) {

            }
        },"");
    }
}
