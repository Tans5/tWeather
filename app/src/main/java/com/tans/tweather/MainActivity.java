package com.tans.tweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetRequestUtils netRequestUtils = NetRequestUtils.newInstance();
        netRequestUtils.setContext(this);
        netRequestUtils.requestForecastInfo("", new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<ForecastBean> results = (List<ForecastBean>) result;
                showLog(results.get(0).getItem().getForecast().getText());
            }

            @Override
            public void onFail(VolleyError e) {

            }
        });
    }

    public void showLog(String s) {
        Log.i(TAG, s + "666666666666666666666");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
