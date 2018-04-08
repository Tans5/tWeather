package com.tans.tweather.manager;

import android.app.Application;
import android.content.SharedPreferences;

import com.tans.tweather.interfaces.ISpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tans on 2017/11/19.
 */

public class SpManager implements ISpManager {


    private List<String> mCommonCities = null;
    private String mCurrentCity = null;
    private int mWallPaperAlpha = 0;
    private SharedPreferences mSp = null;

    private static SpManager instance = null;
    public static String SP_FILE_NAME = "sp_tweather"; //sp文件名字
    public static String SP_KEY_CURRENT_CITY = "current_city";//当前使用城市key
    public static String SP_KEY_COMMON_USE_CITIES = "common_use_cities";//常用城市key
    public static String SP_KEY_WALLPAPER_ALPHA = "wallpaper_alpha";//壁纸透明度key
    public static String SP_KEY_UPDATE_RATE = "update_rate";
    public static String SP_KEY_LOAD_WALLPAPER = "load_wallpaper";
    public static String SP_KEY_OPEN_SERVICE = "open_service";
    public static int  NEVER_UPDATE = -1;

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
    public void storeCommonUseCities(List<String> commonUseCities) {
        mSp.edit().putString(SP_KEY_COMMON_USE_CITIES, commonCities2String(commonUseCities)).commit();
        mCommonCities = commonUseCities;
    }

    @Override
    public List<String> getCommonUseCities() {
        String s = mSp.getString(SP_KEY_COMMON_USE_CITIES, "");
        return stringToCommonCities(s);
    }

    private String commonCities2String(List<String> list) {
        String result = "";
        for(int i=0;i < list.size();i++) {
            if(i != list.size()-1) {
                result = result + list.get(i) + ",";
            } else {
                result = result + list.get(i);
            }
        }
        return  result;
    }

    private List<String> stringToCommonCities (String s) {
        String [] ss = s.split(",");
        List<String> result = new ArrayList<>();

        for(int i =0 ; i<ss.length;i++) {
            if(!ss[i] .equals(""))
                result.add(ss[i]);
        }
        return result;
    }

    @Override
    public void storeCurrentUseCity(String city) {
        mSp.edit().putString(SP_KEY_CURRENT_CITY, city).commit();
        mCurrentCity = city;
    }

    @Override
    public String getCurrentUseCity() {
        mCurrentCity = mSp.getString(SP_KEY_CURRENT_CITY, "");
        return mCurrentCity;
    }

    @Override
    public void storeWallPaperAlpha(int a) {
        mSp.edit().putInt(SP_KEY_WALLPAPER_ALPHA, a).commit();
        mWallPaperAlpha = a;
    }

    @Override
    public int getWallPaperAlpha() {
        mWallPaperAlpha = mSp.getInt(SP_KEY_WALLPAPER_ALPHA, 20);
        return mWallPaperAlpha;
    }

    public void storeUpdateRate(int r) {
        mSp.edit().putInt(SP_KEY_UPDATE_RATE,r).commit();
    }

    public int getUpdateRate() {
        return mSp.getInt(SP_KEY_UPDATE_RATE,1);
    }

    public void storeLoadBing(Boolean b) {
        mSp.edit().putBoolean(SP_KEY_LOAD_WALLPAPER,b).commit();
    }

    public Boolean getLoadBing() {
        return mSp.getBoolean(SP_KEY_LOAD_WALLPAPER,true);
    }

    public void storeOpenService(Boolean b) {
        mSp.edit().putBoolean(SP_KEY_OPEN_SERVICE,b).commit();
    }
    public Boolean getOpenService () {
        return mSp.getBoolean(SP_KEY_OPEN_SERVICE,true);
    }
}
