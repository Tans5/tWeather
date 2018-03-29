package com.tans.tweather.manager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.DateBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.bean.WindBean;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;
import com.tans.tweather.widget.WeatherInfoWidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mine on 2017/11/23.
 */

public class LatestWeatherInfoManager implements ILatestWeatherInfoManager {
    private AtmosphereBean mAtmosphere = null;//大气bean
    private ConditionBean mCondition = null;//天气bean
    private List<ForecastBean> mForecast = null;//预报list
    private WindBean mWind = null;//风bean
    private Context mContext = null;
    private NetRequestUtils mNetRequestUtils = null;//网络请求实例
    private SpManager mSpManager = null;//sp管理实例
    private int mGotItem = 0;//一次天气信息请求（大气，天气，预报，风） 收到的个数
    private String mCurrentCity = "";//当前城市
    private DateBean updateDate = null;//更新日期
    private static LatestWeatherInfoManager instance = null;
    private static int WEATHER_ITEM_NUMBER = 4; //天气请求item总共个数
    private List<WeatherUpdatedListener> mWeatherUpdatedListeners = new ArrayList<WeatherUpdatedListener>();//注册的天气信息变化监听

    /**
     * 天气变化监听
     */
    public interface WeatherUpdatedListener {
        void updated();
    }

    public static LatestWeatherInfoManager newInstance() {
        if (instance == null) {
            instance = new LatestWeatherInfoManager(BaseApplication.getInstance());
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

    /**
     * 发送天气信息变化的广播，小部件接收
     */
    private void sendWeatherUpdatedBroadcast() {
        Intent intent = new Intent();
        intent.setAction(WeatherInfoWidget.UPDATE_WEATHER);
        mContext.sendBroadcast(intent);
    }



    /**
     * 是否位当天的天气信息
     * @return
     */
    public boolean isLatestWeatherInfo() {
        return getCurrentDate().equals(updateDate);
    }

    /**
     * 请求天气更新 activity
     * @param listener
     */
    @Override
    public void updateLatestWeatherInfo(final LatestWeatherUpdateListener listener) {

        if (!mNetRequestUtils.isNetWorkAvailable()) {
            VolleyError volleyError = new VolleyError("网络不可用");
            listener.onFail(volleyError);
            return;
        }

        mNetRequestUtils.requestAtmosphereInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mAtmosphere = (AtmosphereBean) result;
                mGotItem++;
                //判断信息是否接收完毕
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();//更新日期
                    notifyWeatherInfoUpdated();//通知给注册的监听 天气更新
                    listener.onSuccess(); //成功回掉
                    sendWeatherUpdatedBroadcast(); //发送广播
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
                    notifyWeatherInfoUpdated();
                    listener.onSuccess();
                    sendWeatherUpdatedBroadcast();
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
                    notifyWeatherInfoUpdated();
                    listener.onSuccess();
                    sendWeatherUpdatedBroadcast();
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
                    notifyWeatherInfoUpdated();
                    listener.onSuccess();
                    sendWeatherUpdatedBroadcast();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
                listener.onFail(e);
            }
        });
    }

    /**
     * 获取当前日期
     * @return
     */
    private DateBean getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new DateBean(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
    }

    /**
     * 广播调用
     */
    @Override
    public void updateLatestWeatherInfo() {
        mNetRequestUtils.requestAtmosphereInfo(mCurrentCity, new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                mAtmosphere = (AtmosphereBean) result;
                mGotItem++;
                if (mGotItem == WEATHER_ITEM_NUMBER) {
                    mGotItem = 0;
                    updateDate = getCurrentDate();
                    notifyWeatherInfoUpdated();
                    sendWeatherUpdatedBroadcast();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
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
                    notifyWeatherInfoUpdated();
                    sendWeatherUpdatedBroadcast();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
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
                    notifyWeatherInfoUpdated();
                    sendWeatherUpdatedBroadcast();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
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
                    notifyWeatherInfoUpdated();
                    sendWeatherUpdatedBroadcast();
                }
            }

            @Override
            public void onFail(VolleyError e) {
                mGotItem = 0;
            }
        });
    }


    /**
     * 注册天气监听
     * @param listener
     */
    public void registerWeatherUpdateListener(WeatherUpdatedListener listener) {
        if (!mWeatherUpdatedListeners.contains(listener)) {
            mWeatherUpdatedListeners.add(listener);
        }
    }

    /**
     * 取消注册天气监听
     * @param listener
     */
    public void unregisterWeatherUpdateListener(WeatherUpdatedListener listener) {
        if (mWeatherUpdatedListeners.contains(listener))
            mWeatherUpdatedListeners.remove(listener);
    }

    /**
     * 通知监听 天气更新
     */
    private void notifyWeatherInfoUpdated() {
        for (WeatherUpdatedListener listener : mWeatherUpdatedListeners)
            listener.updated();
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
