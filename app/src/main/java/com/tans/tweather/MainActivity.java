package com.tans.tweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.utils.NetRequestUtils;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    LatestWeatherInfoManager latestWeatherInfoManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance(this);
        showLog("isAddedCurrentCity: " + latestWeatherInfoManager.isAddedCurrentCity());
        if (!latestWeatherInfoManager.isAddedCurrentCity()) {
            latestWeatherInfoManager.loadCurrentCity(new ILatestWeatherInfoManager.LoadCurrentCityListener() {
                @Override
                public void onSuccess() {
                    updateWeather();
                }

                @Override
                public void onFail(VolleyError e) {

                }
            });
        } else {
            updateWeather();
        }
    }

    public void updateWeather() {
        latestWeatherInfoManager.updateLatestWeatherInfo(new ILatestWeatherInfoManager.LatestWeatherUpdateListener() {
            @Override
            public void onSuccess() {
                showLog(latestWeatherInfoManager.getmCurrentCity() + ":" + latestWeatherInfoManager.getmCondition().getTemp() + "C  " + latestWeatherInfoManager.getmCondition().getText());
            }

            @Override
            public void onFail(VolleyError e) {

            }
        });
    }

    public void showLog(String s) {
        Log.i(TAG, s + "666666666666666666666");
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
