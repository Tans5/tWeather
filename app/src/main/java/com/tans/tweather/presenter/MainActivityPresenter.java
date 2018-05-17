package com.tans.tweather.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.volley.VolleyError;
import com.tans.tweather.activity.MainActivity;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.DateBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.bean.WindBean;
import com.tans.tweather.component.DaggerMainActivityComponent;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.module.PresenterModule;
import com.tans.tweather.service.UpdateWeatherInfoService;
import com.tans.tweather.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mine on 2018/3/1.
 */

public class MainActivityPresenter {

    public class WeatherVo {
        public AtmosphereBean atmosphere;
        public ConditionBean condition;
        public List<ForecastBean> forecast;
        public WindBean wind;
    }

    WeatherVo mWeatherVo;

    public static String TAG = MainActivityPresenter.class.getSimpleName();

    MainActivityView mView = null;

    @Inject
    LatestWeatherInfoManager latestWeatherInfoManager = null;

    @Inject
    ChinaCitiesManager chinaCitiesManager = null;

    @Inject
    SettingsManager settingsManager;

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
    SettingsManager.SettingsChangeListener settingsChangeListener = new SettingsManager.SettingsChangeListener() {
        @Override
        public void settingsChange() {
            refreshScrim();
            if(!settingsManager.isOpenService()) {
                Intent intent = new Intent((MainActivity) mView, UpdateWeatherInfoService.class);
                BaseApplication.getInstance().stopService(intent);
            }
        }
    };


    public MainActivityPresenter(MainActivityView view) {
        mView = view;
    }

    public void init() {
        DaggerMainActivityComponent.builder()
                .presenterModule(new PresenterModule(mView))
                .applicationComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this);
        settingsManager.registerListener(settingsChangeListener);
        latestWeatherInfoManager.registerWeatherUpdateListener(mWeatherUpdatedListener);
        chinaCitiesManager.registerCommonCitesChangeListener(commonCitesChangeListener);
        chinaCitiesManager.registerCurrentCityChangeListener(currentCitesChangeListener);
    }

    public void refreshScrim() {
        mView.refreshScrim(settingsManager.getAlpha());
    }

    public boolean loadBingImage() {
        return settingsManager.isLoadImage();
    }

    public void loadWeatherInfo(boolean isRefresh) {
        initCommonCites();
        if(settingsManager.isOpenService()) {
            startService();
        }
        if(!isAddedCurrentCity() || chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
            if(Build.VERSION.SDK_INT < 23) {
                loadCurrentCity();
            }
            else {
                mView.requestLocationPermission();
            }
        } else {
            if (!latestWeatherInfoManager.isLatestWeatherInfo() || isRefresh) {
                updateWeather();
            } else {
                mView.setWeatherViewEnable(true);
                mView.refreshWeatherInfo(createWeatherVo());
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

    public void loadCurrentCity() {
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
                    initCommonCites();
                }
                updateWeather();
            }

            @Override
            public void onFail(VolleyError e) {
                ToastUtils.getInstance().showShortText("位置信息请求失败");
                mView.setWeatherViewEnable(false);
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
                mView.setWeatherViewEnable(true);
                mView.refreshWeatherInfo(createWeatherVo());
                ToastUtils.getInstance().showShortText(latestWeatherInfoManager.getmCurrentCity()+": "+ latestWeatherInfoManager.getmCondition().getText()+"  "+latestWeatherInfoManager.getmCondition().getTemp());
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
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

    private WeatherVo createWeatherVo() {
        if(mWeatherVo == null) {
            mWeatherVo.condition = latestWeatherInfoManager.getmCondition();
            mWeatherVo.atmosphere = latestWeatherInfoManager.getmAtmosphere();
            mWeatherVo.forecast = latestWeatherInfoManager.getmForecast();
            mWeatherVo.wind = latestWeatherInfoManager.getmWind();
        }
        return mWeatherVo;
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

    public void saveImage(final Bitmap bitmap) {
        DateBean today = latestWeatherInfoManager.getUpdateDate();
        final String fileName ;
        if(today != null) {
            fileName = today.getYear() + "-" + today.getMonth() + "-" + today.getDay() + ".jpg";
        } else {
            fileName = System.currentTimeMillis()+".jpg";
        }
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                File fileDir = new File(Environment.getExternalStorageDirectory(), "BingImage");
                if(!fileDir.exists()) {
                    fileDir.mkdir();
                }
                File file = new File(fileDir,fileName);
                if(file.exists()) {
                    return null;
                } else {
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ToastUtils.getInstance().showShortText("保存成功");
            }
        }.execute();
    }

    public void destroy() {
        mView = null;
        latestWeatherInfoManager.unregisterWeatherUpdateListener(mWeatherUpdatedListener);
        chinaCitiesManager.unregisterCommCitesChangeListener(commonCitesChangeListener);
        chinaCitiesManager.unregisterCurrentCityChangeListenter(currentCitesChangeListener);
        settingsManager.unregisterlistener(settingsChangeListener);
    }
}
