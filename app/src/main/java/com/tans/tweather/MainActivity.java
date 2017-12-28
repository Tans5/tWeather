package com.tans.tweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.VolleyError;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.service.UpdateWeatherInfoService;
import com.tans.tweather.utils.ToastUtils;
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
        if (UpdateWeatherInfoService.getInstance() == null) {
            Log.i(TAG, "is service running:" + null);
            Intent intent = new Intent(this, UpdateWeatherInfoService.class);
            startService(intent);
        }
        ChinaCitiesManager.CityTest();
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
                latestWeatherInfoManager.registerWeatherUpdateListener(mWeatherUpdatedListener);
            }

            @Override
            public void onFail(VolleyError e) {

            }
        });
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
