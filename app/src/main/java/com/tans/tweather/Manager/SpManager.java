package com.tans.tweather.Manager;

import android.app.Application;
import android.content.SharedPreferences;

import com.tans.tweather.Interface.ISpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tans on 2017/11/19.
 */

public class SpManager implements ISpManager {

    List<CommonUseCitiesChangeListener> commonUseCitiesChangeListeners = null;
    List<CurrentUseCityChangeListener> currentUseCityChangeListeners = null;
    List<WallPaperAlphaChangeListener> wallPaperAlphaChangeListeners = null;

    private Set<String> mCommonCities = null;
    private String mCurrentCity = null;
    private int mWallPaperAlpha = 0;
    private SharedPreferences mSp = null;

    private static SpManager instance = null;
    public static String SP_FILE_NAME = "sp_tweather";
    public static String SP_KEY_CURRENT_CITY = "current_city";
    public static String SP_KEY_COMMON_USE_CITIES = "common_use_cities";
    public static String SP_KEY_WALLPAPER_ALPHA = "wallpaper_alpha";


    public static SpManager newInstance() {
        if (instance == null) {
            instance = new SpManager();
        }
        return instance;
    }

    private SpManager() {

    }

    public void initSp(Application context) {
        if (mSp == null)
            mSp = context.getSharedPreferences(SP_FILE_NAME, context.MODE_PRIVATE);
    }

    @Override
    public void storeCommonUseCities(Set<String> commonUseCities) {
        mSp.edit().putStringSet(SP_KEY_COMMON_USE_CITIES,commonUseCities).commit();
        mCommonCities = commonUseCities;
        notifyCommonUseCitiesChange();
    }

    @Override
    public Set<String> getCommonUseCities() {
        mCommonCities = mSp.getStringSet(SP_KEY_COMMON_USE_CITIES,null);
        return mCommonCities;
    }

    @Override
    public void storeCurrentUseCity(String city) {
        mSp.edit().putString(SP_KEY_CURRENT_CITY,city).commit();
        mCurrentCity = city;
        notifyCurrentUseCityChange();
    }

    @Override
    public String getCurrentUseCity() {
        mCurrentCity = mSp.getString(SP_KEY_CURRENT_CITY,"");
        return mCurrentCity;
    }

    @Override
    public void storeWallPaperAlpha(int a) {
        mSp.edit().putInt(SP_KEY_WALLPAPER_ALPHA,a);
        mWallPaperAlpha = a;
        notifyWallPaperAlphaChange();
    }

    @Override
    public int getWallPaperAlpha() {
        mWallPaperAlpha = mSp.getInt(SP_KEY_WALLPAPER_ALPHA,0);
        return mWallPaperAlpha;
    }


    public void registerCommonUseCitesChangeListener(CommonUseCitiesChangeListener listener) {
        if (commonUseCitiesChangeListeners == null) {
            commonUseCitiesChangeListeners = new ArrayList<CommonUseCitiesChangeListener>();
        }
        commonUseCitiesChangeListeners.add(listener);
    }

    public void unregisterCommonUseCitesChangeListener(CommonUseCitiesChangeListener listener) {
        if (commonUseCitiesChangeListeners == null) {
            return;
        }
        commonUseCitiesChangeListeners.remove(listener);
    }

    private void notifyCommonUseCitiesChange() {
        if (commonUseCitiesChangeListeners != null) {
            for (CommonUseCitiesChangeListener listener : commonUseCitiesChangeListeners) {
                listener.onChange(mCommonCities);
            }
        }
    }

    public void registerCurrentUseCityChangeListener(CurrentUseCityChangeListener listener) {
        if (currentUseCityChangeListeners == null) {
            currentUseCityChangeListeners = new ArrayList<CurrentUseCityChangeListener>();
        }
        currentUseCityChangeListeners.add(listener);
    }

    public void unregisterCurrentUseCityChangeListener(CurrentUseCityChangeListener listener) {
        if (currentUseCityChangeListeners == null) {
            return;
        }
        currentUseCityChangeListeners.remove(listener);
    }

    private void notifyCurrentUseCityChange() {
        if (currentUseCityChangeListeners != null) {
            for (CurrentUseCityChangeListener listener : currentUseCityChangeListeners) {
                listener.onChange(mCurrentCity);
            }
        }
    }


    public void registerWallPaperAlphaChangeListener(WallPaperAlphaChangeListener listener) {
        if (wallPaperAlphaChangeListeners == null) {
            wallPaperAlphaChangeListeners = new ArrayList<WallPaperAlphaChangeListener>();
        }
        wallPaperAlphaChangeListeners.add(listener);
    }

    public void unregisterWallPaperAlphaChangeListener(WallPaperAlphaChangeListener listener) {
        if (wallPaperAlphaChangeListeners == null) {
            return;
        }
        wallPaperAlphaChangeListeners.remove(listener);
    }

    private void notifyWallPaperAlphaChange() {
        if (wallPaperAlphaChangeListeners != null) {
            for (WallPaperAlphaChangeListener listener : wallPaperAlphaChangeListeners) {
                listener.onChange(mWallPaperAlpha);
            }
        }
    }
}
