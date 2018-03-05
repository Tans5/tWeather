package com.tans.tweather.presenter;

import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.tans.tweather.activity.MainActivity;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SpManager;
import com.tans.tweather.service.UpdateWeatherInfoService;
import com.tans.tweather.utils.ToastUtils;

/**
 * Created by mine on 2018/3/1.
 */

public class MainActivityPresenter {
    public static String TAG = MainActivityPresenter.class.getSimpleName();

    MainActivityView mView = null;
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    SpManager spManager = null;
    ChinaCitiesManager chinaCitiesManager = null;

    LatestWeatherInfoManager.WeatherUpdatedListener mWeatherUpdatedListener = new LatestWeatherInfoManager.WeatherUpdatedListener() {
        @Override
        public void updated() {

        }
    };

    public MainActivityPresenter(MainActivityView view) {
        mView = view;
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        spManager = SpManager.newInstance();
        chinaCitiesManager = new ChinaCitiesManager();
    }

    public void loadWeatherInfo() {
        startService();
        if(!isAddedCurrentCity()) {
            loadCurrentCity();
        } else {
            if (!latestWeatherInfoManager.isLatestWeatherInfo())
                updateWeather();
        }
    }

    private void startService() {
        if (UpdateWeatherInfoService.getInstance() == null) {
            Log.i(TAG, "is service running:" + null);
            Intent intent = new Intent((MainActivity) mView, UpdateWeatherInfoService.class);
            ((MainActivity)mView).startService(intent);
        }
    }

    private void loadCurrentCity() {
        chinaCitiesManager.loadCurrentCity(new ChinaCitiesManager.LoadCurrentCityListener() {
            @Override
            public void onSuccess(String s) {
                spManager.storeCurrentUseCity(s);
                latestWeatherInfoManager.setmCurrentCity(s);
                updateWeather();
            }

            @Override
            public void onFail(VolleyError e) {
                ToastUtils.getInstance().showShortText(e.getMessage());
            }
        });
    }

    private void updateWeather() {
        latestWeatherInfoManager.updateLatestWeatherInfo(new ILatestWeatherInfoManager.LatestWeatherUpdateListener() {
            @Override
            public void onSuccess() {
             //   showLog(latestWeatherInfoManager.getmCurrentCity() + ":" + latestWeatherInfoManager.getmCondition().getTemp() + "C  " + latestWeatherInfoManager.getmCondition().getText());
                latestWeatherInfoManager.registerWeatherUpdateListener(mWeatherUpdatedListener);
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                ToastUtils.getInstance().showShortText(e.getMessage());
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
            }
        });
    }

    private boolean isAddedCurrentCity() {
        String currentCity = spManager.getCurrentUseCity();
        if (currentCity.equals(""))
            return false;
        else {
            latestWeatherInfoManager.setmCurrentCity(currentCity);
            return true;
        }
    }

    public void destroy() {
        mView = null;
        latestWeatherInfoManager.unregisterWeatherUpdateListener(mWeatherUpdatedListener);
    }
}
