package com.tans.tweather.Interface;

import java.util.Set;

/**
 * Created by tans on 2017/11/19.
 */

public interface ISpManager {

    public static interface CommonUseCitiesChangeListener
    {
        public void onChange(Set<String> cities);
    }
    public static interface CurrentUseCityChangeListener
    {
        public void onChange(String city );
    }
    public static interface WallPaperAlphaChangeListener
    {
        public void onChange(int a);
    }

    public void storeCommonUseCities(Set<String> commonUseCities);
    public Set<String> getCommonUseCities();

    public void storeCurrentUseCity(String city);
    public String getCurrentUseCity();

    public void storeWallPaperAlpha(int a);
    public int getWallPaperAlpha();
}
