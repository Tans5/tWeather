package com.tans.tweather.iviews;

/**
 * Created by mine on 2018/3/1.
 */

public interface MainActivityView {
    boolean isRefreshing();
    void closeRefreshing();
    void refreshWeatherInfo();
}
