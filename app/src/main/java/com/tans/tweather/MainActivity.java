package com.tans.tweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.service.UpdateWeatherInfoService;
import com.tans.tweather.utils.ToastUtils;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    LatestWeatherInfoManager.WeatherUpdatedListener mWeatherUpdatedListener = new LatestWeatherInfoManager.WeatherUpdatedListener() {
        @Override
        public void updated() {
            showLog("weather updated");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (UpdateWeatherInfoService.getInstance() == null) {
            Log.i(TAG, "is service running:" + null);
            Intent intent = new Intent(this, UpdateWeatherInfoService.class);
            startService(intent);
        }

        new ChinaCitiesManager();
        if (UpdateWeatherInfoService.getInstance() != null)
            Log.i(TAG, "is service running:" + true);
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        showLog("isAddedCurrentCity: " + latestWeatherInfoManager.isAddedCurrentCity());
        if (!latestWeatherInfoManager.isAddedCurrentCity()) {
            latestWeatherInfoManager.loadCurrentCity(new ILatestWeatherInfoManager.LoadCurrentCityListener() {
                @Override
                public void onSuccess() {
                    updateWeather();
                }

                @Override
                public void onFail(VolleyError e) {
                    ToastUtils.getInstance().showShortText(e.getMessage());
                }
            });
        } else {
            if (!latestWeatherInfoManager.isLatestWeatherInfo())
                updateWeather();
        }
    }

    public void updateWeather() {
        latestWeatherInfoManager.updateLatestWeatherInfo(new ILatestWeatherInfoManager.LatestWeatherUpdateListener() {
            @Override
            public void onSuccess() {
                showLog(latestWeatherInfoManager.getmCurrentCity() + ":" + latestWeatherInfoManager.getmCondition().getTemp() + "C  " + latestWeatherInfoManager.getmCondition().getText());
                latestWeatherInfoManager.registerWeatherUpdateListener(mWeatherUpdatedListener);
            }

            @Override
            public void onFail(VolleyError e) {
                ToastUtils.getInstance().showShortText(e.getMessage());
            }
        });
    }

    public void updateWeather(View v) {
        updateWeather();
    }

    public void showLog(String s) {
        Log.i(TAG, s + "666666666666666666666");
        ToastUtils.getInstance().showShortText(s);
    }

    @Override
    protected void onDestroy() {
        latestWeatherInfoManager.unregisterWeatherUpdateListener(mWeatherUpdatedListener);
        super.onDestroy();
    }
}
