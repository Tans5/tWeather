package com.tans.tweather.interfaces;

import java.util.Set;

/**
 * Created by tans on 2017/11/19.
 */

public interface ISpManager {

    interface CommonUseCitiesChangeListener {
        void onChange(Set<String> cities);
    }

    interface CurrentUseCityChangeListener {
        void onChange(String city);
    }

    interface WallPaperAlphaChangeListener {
        void onChange(int a);
    }

    void storeCommonUseCities(Set<String> commonUseCities);

    Set<String> getCommonUseCities();

    void storeCurrentUseCity(String city);

    String getCurrentUseCity();

    void storeWallPaperAlpha(int a);

    int getWallPaperAlpha();
}
