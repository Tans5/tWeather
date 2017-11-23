package com.tans.tweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance(this);
        showLog(""+latestWeatherInfoManager.isLatestWeatherInfo());
        latestWeatherInfoManager.updateLatestWeatherInfo(new ILatestWeatherInfoManager.LatestWeatherUpdateListener() {
            @Override
            public void onSuccess() {
                showLog(latestWeatherInfoManager.getmCondition().getTemp()+" ");
            }

            @Override
            public void onFail(VolleyError e) {

            }
        });
    }

    @Override
    protected void onResume() {
        showLog(""+latestWeatherInfoManager.isLatestWeatherInfo());
        super.onResume();
    }

    public void showLog(String s) {
        Log.i(TAG, s + "666666666666666666666");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
