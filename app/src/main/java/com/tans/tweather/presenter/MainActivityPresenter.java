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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mine on 2018/3/1.
 */

public class MainActivityPresenter {
    public static String TAG = MainActivityPresenter.class.getSimpleName();

    MainActivityView mView = null;
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    ChinaCitiesManager chinaCitiesManager = null;

    LatestWeatherInfoManager.WeatherUpdatedListener mWeatherUpdatedListener = new LatestWeatherInfoManager.WeatherUpdatedListener() {
        @Override
        public void updated() {

        }
    };

    ChinaCitiesManager.CommonCitesChangeListener commonCitesChangeListener = new ChinaCitiesManager.CommonCitesChangeListener() {
        @Override
        public void onChange() {
            mView.refreshMenuCites(chinaCitiesManager.getCommonCities(),chinaCitiesManager.getCurrentCity());
        }
    };
    ChinaCitiesManager.CurrentCitesChangeListener currentCitesChangeListener = new ChinaCitiesManager.CurrentCitesChangeListener() {
        @Override
        public void onChange() {
            if(!chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION))
                latestWeatherInfoManager.setmCurrentCity(chinaCitiesManager.getCurrentCity());
            loadWeatherInfo(true);
        }
    };


    public MainActivityPresenter(MainActivityView view) {
        mView = view;
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        chinaCitiesManager = ChinaCitiesManager.newInstance();
    }

    public void loadWeatherInfo(boolean isRefresh) {
        startService();
        if(!isAddedCurrentCity() || chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
            loadCurrentCity();
        } else {
            if (!latestWeatherInfoManager.isLatestWeatherInfo() || isRefresh) {
                updateWeather();
            } else {
                mView.setWeatherViewEnable(true);
                mView.refreshWeatherInfo();
                chinaCitiesManager.registerCommonCitesChangeListener(commonCitesChangeListener);
                chinaCitiesManager.registerCurrentCityChangeListener(currentCitesChangeListener);
            }
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
        if(!mView.isRefreshing()) {
            mView.startRefreshing();
        }
        chinaCitiesManager.loadCurrentCity(new ChinaCitiesManager.LoadCurrentCityListener() {
            @Override
            public void onSuccess(String s) {
                latestWeatherInfoManager.setmCurrentCity(s);
                if(!chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
                    chinaCitiesManager.setCurrentCity(s);
                    List<String> cities = new ArrayList<>();
                    cities.add(s);
                    chinaCitiesManager.setCommonCities(cities);
                }
                updateWeather();
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

    private void updateWeather() {
        if(!mView.isRefreshing()) {
            mView.startRefreshing();
        }
        latestWeatherInfoManager.updateLatestWeatherInfo(new ILatestWeatherInfoManager.LatestWeatherUpdateListener() {
            @Override
            public void onSuccess() {
             //   showLog(latestWeatherInfoManager.getmCurrentCity() + ":" + latestWeatherInfoManager.getmCondition().getTemp() + "C  " + latestWeatherInfoManager.getmCondition().getText());
                latestWeatherInfoManager.registerWeatherUpdateListener(mWeatherUpdatedListener);
                mView.setWeatherViewEnable(true);
                mView.refreshWeatherInfo();
                ToastUtils.getInstance().showShortText(latestWeatherInfoManager.getmCurrentCity()+": "+ latestWeatherInfoManager.getmCondition().getText()+"  "+latestWeatherInfoManager.getmCondition().getTemp());
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
                chinaCitiesManager.registerCommonCitesChangeListener(commonCitesChangeListener);
                chinaCitiesManager.registerCurrentCityChangeListener(currentCitesChangeListener);
            }

            @Override
            public void onFail(VolleyError e) {
                mView.setWeatherViewEnable(false);
                ToastUtils.getInstance().showShortText(e.getMessage());
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
            }
        });
    }

    private boolean isAddedCurrentCity() {
        String currentCity = chinaCitiesManager.getCurrentCity();
        if (currentCity.equals(""))
            return false;
        else {
            return true;
        }
    }

    public void initCommonCites() {
        List<String> commonCites = chinaCitiesManager.getCommonCities();
        String currentCity = chinaCitiesManager.getCurrentCity();
        mView.refreshMenuCites(commonCites,currentCity);
    }

    public List<String> getCommonCites() {
        return chinaCitiesManager.getCommonCities();
    }

    public void removeCommonCity(String city) {
        List<String> commonCities = chinaCitiesManager.getCommonCities();
        commonCities.remove(city);
        chinaCitiesManager.setCommonCities(commonCities);
    }

    public void changeCurrentCity(String city) {
        chinaCitiesManager.setCurrentCity(city);
    }

    public void destroy() {
        mView = null;
        latestWeatherInfoManager.unregisterWeatherUpdateListener(mWeatherUpdatedListener);
        chinaCitiesManager.unregisterCommCitesChangeListener(commonCitesChangeListener);
        chinaCitiesManager.unregisterCurrentCityChangeListenter(currentCitesChangeListener);
    }
}
