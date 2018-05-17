package com.tans.tweather.iviews;

import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.presenter.MainActivityPresenter;

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
}
