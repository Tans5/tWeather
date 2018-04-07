package com.tans.tweather.iviews;

import com.tans.tweather.database.bean.LocationBean;

import java.util.List;

/**
 * Created by mine on 2018/3/1.
 */

public interface MainActivityView extends View {
    boolean isRefreshing();
    void closeRefreshing();
    void startRefreshing();
    void refreshWeatherInfo();
    void setWeatherViewEnable(boolean b);
    void refreshMenuCites(List<String> cites,String currentCity);
}
