package com.tans.tweather.manager;

import android.app.Application;
import android.content.Context;

import com.android.volley.VolleyError;
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.DateBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.bean.WindBean;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mine on 2017/11/23.
 */

public class LatestWeatherInfoManager implements ILatestWeatherInfoManager {
    private AtmosphereBean mAtmosphere = null;
    private ConditionBean mCondition = null;
    private List<ForecastBean> mForecast = null;
    private WindBean mWind = null;
    private Context mContext = null;
    private NetRequestUtils mNetRequestUtils = null;
    private SpManager mSpManager = null;
    private int mGotItem = 0;
    private String mCurrentCity = "成都市";
    private DateBean updateDate = null;
    private static LatestWeatherInfoManager instance = null;
    private static int WEATHER_ITEM_NUMBER = 4;

    public static LatestWeatherInfoManager newInstance(Context context) {
        if (instance == null) {
            instance = new LatestWeatherInfoManager(context);
        }
        return instance;
    }

    private LatestWeatherInfoManager(Context context) {
        this.mContext = context;
        mNetRequestUtils = NetRequestUtils.newInstance();
        mNetRequestUtils.setContext(mContext);

        mSpManager = SpManager.newInstance();
        mSpManager.initSp((Application) context.getApplicationContext());
    }

    public boolean isNetWorkAvailable() {
        return mNetRequestUtils.isNetWorkAvailable();
    }

    public boolean isAddedCurrentCity() {
        String currentCity = mSpManager.getCurrentUseCity();
        if (currentCity.equals(""))
            return false;
        else {
            mCurrentCity = currentCity;
            return true;
        }
    }

    public boolean isLatestWeatherInfo() {
        return getCurrentDate().equals(updateDate);
    }

    @Override
    public void updateLatestWeatherInfo(final LatestWeatherUpdateListener listener) {
        mNetRequestUtils.requestAtmosphereInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mAtmosphere = (AtmosphereBean) result;
                mGotItem++;
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();
                    listener.onSuccess();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
                listener.onFail(e);
            }
        });
        mNetRequestUtils.requestConditionInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mCondition = (ConditionBean) result;
                mGotItem++;
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();
                    listener.onSuccess();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
                listener.onFail(e);
            }
        });
        mNetRequestUtils.requestForecastInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mForecast = (ArrayList<ForecastBean>) result;
                mGotItem++;
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();
                    listener.onSuccess();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
                listener.onFail(e);
            }
        });
        mNetRequestUtils.requestWindInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mWind = (WindBean) result;
                mGotItem++;
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();
                    listener.onSuccess();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
                listener.onFail(e);
            }
        });
    }

    private DateBean getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new DateBean(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
    }

    @Override
    public void updateLatestWeatherInfo() {

    }

    public void loadCurrentCity(final LoadCurrentCityListener listener) {
        mNetRequestUtils.requestLocationInfo(new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {

                listener.onSuccess();
                mCurrentCity = result.toString();
                mSpManager.storeCurrentUseCity(result.toString());
            }

            @Override
            public void onFail(VolleyError e) {
                listener.onFail(e);
            }
        });
    }

    public AtmosphereBean getmAtmosphere() {
        return mAtmosphere;
    }

    public void setmAtmosphere(AtmosphereBean mAtmosphere) {
        this.mAtmosphere = mAtmosphere;
    }

    public ConditionBean getmCondition() {
        return mCondition;
    }

    public void setmCondition(ConditionBean mCondition) {
        this.mCondition = mCondition;
    }

    public List<ForecastBean> getmForecast() {
        return mForecast;
    }

    public void setmForecast(List<ForecastBean> mForecast) {
        this.mForecast = mForecast;
    }

    public WindBean getmWind() {
        return mWind;
    }

    public void setmWind(WindBean mWind) {
        this.mWind = mWind;
    }

    public void setmCurrentCity(String mCurrentCity) {
        this.mCurrentCity = mCurrentCity;
    }

    public String getmCurrentCity() {
        return mCurrentCity;
    }
}
