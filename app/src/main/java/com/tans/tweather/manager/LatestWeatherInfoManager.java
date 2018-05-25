package com.tans.tweather.manager;

import android.content.Context;
import android.content.Intent;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.weather.AtmosphereBean;
import com.tans.tweather.bean.weather.ConditionBean;
import com.tans.tweather.bean.DateBean;
import com.tans.tweather.bean.weather.ForecastBean;
import com.tans.tweather.bean.weather.WindBean;
import com.tans.tweather.utils.UrlUtils;
import com.tans.tweather.utils.httprequest.BaseHttpRequestUtils;
import com.tans.tweather.widget.WeatherInfoWidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mine on 2017/11/23.
 */

public class LatestWeatherInfoManager {
    private AtmosphereBean mAtmosphere = null;//大气bean
    private ConditionBean mCondition = null;//天气bean
    private List<ForecastBean> mForecast = null;//预报list
    private WindBean mWind = null;//风bean
    private Context mContext = null;
    private BaseHttpRequestUtils mHttpRequestUtils = null;
    private int mGotItem = 0;//一次天气信息请求（大气，天气，预报，风） 收到的个数
    private String mCurrentCity = "";//当前城市
    private DateBean updateDate = null;//更新日期
    private static LatestWeatherInfoManager instance = null;
    private static int WEATHER_ITEM_NUMBER = 4; //天气请求item总共个数
    private List<WeatherUpdatedListener> mWeatherUpdatedListeners = new ArrayList<WeatherUpdatedListener>();//注册的天气信息变化监听


    /**
     * 天气更新的监听
     */
    public interface WeatherRequestListener {
        void onSuccess();
        void onFail(String e);
    }

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

    public void initDependencies(BaseHttpRequestUtils httpRequestUtils) {
        mHttpRequestUtils = httpRequestUtils;
    }

    public DateBean getUpdateDate() {
        return updateDate;
    }

    public AtmosphereBean getAtmosphere() {
        return mAtmosphere;
    }

    public void setAtmosphere(AtmosphereBean mAtmosphere) {
        this.mAtmosphere = mAtmosphere;
    }

    public ConditionBean getCondition() {
        return mCondition;
    }

    public void setCondition(ConditionBean mCondition) {
        this.mCondition = mCondition;
    }

    public List<ForecastBean> listForecast() {
        return mForecast;
    }

    public void setForecast(List<ForecastBean> mForecast) {
        this.mForecast = mForecast;
    }

    public WindBean getWind() {
        return mWind;
    }

    public void setWind(WindBean mWind) {
        this.mWind = mWind;
    }

    public void setCurrentCity(String mCurrentCity) {
        this.mCurrentCity = mCurrentCity;
    }

    public String getCurrentCity() {
        return mCurrentCity;
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
    public void updateLatestWeatherInfo(final WeatherRequestListener listener) {

        if (!mHttpRequestUtils.isNetWorkAvailable()) {
            listener.onFail("网络不可用");
            return;
        }

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createAtmosphereParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<AtmosphereBean>() {
                    @Override
                    public void onSuccess(AtmosphereBean result) {
                        mAtmosphere = result;
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
                    public Class getResultType() {
                        return AtmosphereBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                        listener.onFail(e);
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createConditionParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<ConditionBean>() {
                    @Override
                    public void onSuccess(ConditionBean result) {
                        mCondition = result;
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
                    public Class getResultType() {
                        return ConditionBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                        listener.onFail(e);
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createWindParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<WindBean>() {
                    @Override
                    public void onSuccess(WindBean result) {
                        mWind = result;
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
                    public Class<WindBean> getResultType() {
                        return WindBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                        listener.onFail(e);
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createForecastParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<List>() {
                    @Override
                    public void onSuccess(List result) {
                        mForecast = result;
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
                    public Class<List> getResultType() {
                        return List.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                        listener.onFail(e);
                    }
                });

    }

    /**
     * 广播调用
     */
    public void updateLatestWeatherInfo() {
        if (!mHttpRequestUtils.isNetWorkAvailable()) {
            return;
        }

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createAtmosphereParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<AtmosphereBean>() {
                    @Override
                    public void onSuccess(AtmosphereBean result) {
                        mAtmosphere = result;
                        mGotItem++;
                        //判断信息是否接收完毕
                        if (mGotItem == WEATHER_ITEM_NUMBER) {
                            mGotItem = 0;
                            updateDate = getCurrentDate();//更新日期
                            notifyWeatherInfoUpdated();//通知给注册的监听 天气更新
                            sendWeatherUpdatedBroadcast(); //发送广播
                        }
                    }

                    @Override
                    public Class getResultType() {
                        return AtmosphereBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createConditionParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<ConditionBean>() {
                    @Override
                    public void onSuccess(ConditionBean result) {
                        mCondition = result;
                        mGotItem++;
                        //判断信息是否接收完毕
                        if (mGotItem == WEATHER_ITEM_NUMBER) {
                            mGotItem = 0;
                            updateDate = getCurrentDate();//更新日期
                            notifyWeatherInfoUpdated();//通知给注册的监听 天气更新
                            sendWeatherUpdatedBroadcast(); //发送广播
                        }
                    }

                    @Override
                    public Class getResultType() {
                        return ConditionBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createWindParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<WindBean>() {
                    @Override
                    public void onSuccess(WindBean result) {
                        mWind = result;
                        mGotItem++;
                        //判断信息是否接收完毕
                        if (mGotItem == WEATHER_ITEM_NUMBER) {
                            mGotItem = 0;
                            updateDate = getCurrentDate();//更新日期
                            notifyWeatherInfoUpdated();//通知给注册的监听 天气更新
                            sendWeatherUpdatedBroadcast(); //发送广播
                        }
                    }

                    @Override
                    public Class<WindBean> getResultType() {
                        return WindBean.class;
                    }

                    @Override
                    public void onFail(String e) {
                        mGotItem = 0;
                    }
                });

        mHttpRequestUtils.request(UrlUtils.getWeatherBaseUrl(),
                UrlUtils.getWeatherPath(),
                BaseHttpRequestUtils.HttpRequestMethod.GET,
                UrlUtils.createForecastParams(mCurrentCity),
                new BaseHttpRequestUtils.HttpRequestListener<List>() {
                    @Override
                    public void onSuccess(List result) {
                        mForecast = result;
                        mGotItem++;
                        //判断信息是否接收完毕
                        if (mGotItem == WEATHER_ITEM_NUMBER) {
                            mGotItem = 0;
                            updateDate = getCurrentDate();//更新日期
                            notifyWeatherInfoUpdated();//通知给注册的监听 天气更新
                            sendWeatherUpdatedBroadcast(); //发送广播
                        }
                    }

                    @Override
                    public Class<List> getResultType() {
                        return List.class;
                    }

                    @Override
                    public void onFail(String e) {
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

    private LatestWeatherInfoManager(Context context) {
        this.mContext = context;
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
     * 获取当前日期
     * @return
     */
    private DateBean getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new DateBean(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
    }

    /**
     * 通知监听 天气更新
     */
    private void notifyWeatherInfoUpdated() {
        for (WeatherUpdatedListener listener : mWeatherUpdatedListeners)
            listener.updated();
    }
}
