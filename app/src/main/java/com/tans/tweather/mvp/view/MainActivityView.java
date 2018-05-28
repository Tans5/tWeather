package com.tans.tweather.mvp.view;

import com.tans.tweather.activity.main.presenter.MainActivityPresenter;
import com.tans.tweather.bean.account.UserBean;
import com.tans.tweather.mvp.View;

import java.util.List;

/**
 * Created by mine on 2018/3/1.
 */

public interface MainActivityView extends View {
    boolean isRefreshing();
    void closeRefreshing();
    void startRefreshing();
    void refreshWeatherInfo(MainActivityPresenter.WeatherVo weatherVo);
    void setWeatherViewEnable(boolean b);
    void refreshMenuCites(List<String> cites,String currentCity);
    void refreshScrim(int alpha);
    void requestLocationPermission();
    void showToast(String msg);
    void signUpSuccess(UserBean userBean);
    void signUpFail();
    void logInSuccess(UserBean userBean);
    void logInFail();
}
