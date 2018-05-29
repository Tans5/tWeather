package com.tans.tweather.activity.main.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.tans.tweather.activity.main.MainActivity;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.account.UserBean;
import com.tans.tweather.bean.weather.AtmosphereBean;
import com.tans.tweather.bean.weather.ConditionBean;
import com.tans.tweather.bean.DateBean;
import com.tans.tweather.bean.weather.ForecastBean;
import com.tans.tweather.bean.weather.WindBean;
import com.tans.tweather.dagger2.component.DaggerMainActivityComponent;
import com.tans.tweather.manager.UserAccountManager;
import com.tans.tweather.mvp.Presenter;
import com.tans.tweather.mvp.view.MainActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.dagger2.module.PresenterModule;
import com.tans.tweather.service.UpdateWeatherInfoService;
import com.tans.tweather.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mine on 2018/3/1.
 */

public class MainActivityPresenter implements Presenter {

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

    @Inject
    UserAccountManager userAccountManager;

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
                latestWeatherInfoManager.setCurrentCity(chinaCitiesManager.getCurrentCity());
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

    @Override
    public void initDependencies() {
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

    public void loadCurrentCity() {
        if(!mView.isRefreshing()) {
            mView.startRefreshing();
        }
        chinaCitiesManager.loadCurrentCity(new ChinaCitiesManager.LoadCurrentCityListener() {
            @Override
            public void onSuccess(String s) {
                latestWeatherInfoManager.setCurrentCity(s);
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
            public void onFail(String e) {
                ToastUtils.getInstance().showShortText("位置信息请求失败");
                mView.setWeatherViewEnable(false);
                if(mView.isRefreshing()) {
                    mView.closeRefreshing();
                }
            }
        });
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
        Observable.just(bitmap)
                .observeOn(Schedulers.io())
                .map(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        Boolean result = true;
                        DateBean today = latestWeatherInfoManager.getUpdateDate();
                        final String fileName ;
                        if(today != null) {
                            fileName = today.getYear() + "-" + today.getMonth() + "-" + today.getDay() + ".jpg";
                        } else {
                            fileName = System.currentTimeMillis()+".jpg";
                        }
                        File fileDir = new File(Environment.getExternalStorageDirectory(), "BingImage");
                        if(!fileDir.exists()) {
                            fileDir.mkdir();
                        }
                        File file = new File(fileDir,fileName);
                        if(file.exists()) {
                            result = false;
                        } else {
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                result = false;
                            }
                        }
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean) {
                            mView.showToast("图片保存成功！！");
                        } else {
                            mView.showToast("图片保存失败～");
                        }
                    }
                });
    }

    public void signUp(String name, String password) {
        UserBean user = new UserBean();
        user.setName(name);
        user.setPassword(password);
        userAccountManager.signUp(user, new UserAccountManager.SignUpListener() {
            @Override
            public void onSuccess() {
                mView.signUpSuccess(userAccountManager.getUser());
            }

            @Override
            public void onFail(String e) {
                mView.showToast(e);
                mView.signUpFail();
            }
        });
    }

    public void logIn(String name, String password) {
        UserBean user = new UserBean();
        user.setName(name);
        user.setPassword(password);
        userAccountManager.logIn(user, new UserAccountManager.LoginListener() {
            @Override
            public void onSuccess() {
                mView.logInSuccess(userAccountManager.getUser());
            }

            @Override
            public void onFail(String e) {
                mView.showToast(e);
                mView.logInFail();
            }
        });
    }

    public void logOut() {
        userAccountManager.logOut();
    }

    @Override
    public void onDestroy() {
        mView = null;
        latestWeatherInfoManager.unregisterWeatherUpdateListener(mWeatherUpdatedListener);
        chinaCitiesManager.unregisterCommCitesChangeListener(commonCitesChangeListener);
        chinaCitiesManager.unregisterCurrentCityChangeListener(currentCitesChangeListener);
        settingsManager.unregisterListener(settingsChangeListener);
    }

    private void startService() {
        if (UpdateWeatherInfoService.getInstance() == null) {
            Log.i(TAG, "is service running:" + null);
            Intent intent = new Intent((MainActivity) mView, UpdateWeatherInfoService.class);
            ((MainActivity)mView).startService(intent);
        }
    }

    private void updateWeather() {
        if(!mView.isRefreshing()) {
            mView.startRefreshing();
        }
        latestWeatherInfoManager.updateLatestWeatherInfo(new LatestWeatherInfoManager.WeatherRequestListener() {
            @Override
            public void onSuccess() {
                if(mView != null) {
                    mView.setWeatherViewEnable(true);
                    mView.refreshWeatherInfo(createWeatherVo());
                    ToastUtils.getInstance().showShortText(latestWeatherInfoManager.getCurrentCity() + ": " + latestWeatherInfoManager.getCondition().getText() + "  " + latestWeatherInfoManager.getCondition().getTemp());
                    if (mView.isRefreshing()) {
                        mView.closeRefreshing();
                    }
                }
            }

            @Override
            public void onFail(String e) {
                if(mView != null) {
                    mView.setWeatherViewEnable(false);
                    ToastUtils.getInstance().showShortText(e);
                    if (mView.isRefreshing()) {
                        mView.closeRefreshing();
                    }
                }
            }
        });
    }

    private WeatherVo createWeatherVo() {
        if(mWeatherVo == null) {
            mWeatherVo = new WeatherVo();
        }
        mWeatherVo.condition = latestWeatherInfoManager.getCondition();
        mWeatherVo.atmosphere = latestWeatherInfoManager.getAtmosphere();
        mWeatherVo.forecast = latestWeatherInfoManager.listForecast();
        mWeatherVo.wind = latestWeatherInfoManager.getWind();
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
}
