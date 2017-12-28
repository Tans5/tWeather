package com.tans.tweather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.tans.tweather.R;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.utils.ResultTransUtils;


/**
 * Created by tans on 2017/12/3.
 */

public class WeatherInfoWidget extends AppWidgetProvider {

    public static String CLICK_FOR_UPDATE = "com.tans.tweather.CLICK_FOR_UPDATE";//桌面button点击更新 广播
    public static String UPDATE_WEATHER = "com.tans.tweather.UPDATE_WEATHER";//天气更新广播
    public static String TAG = WeatherInfoWidget.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        //收到天气更新广播
        if (intent.getAction().equals(UPDATE_WEATHER))
            updateAllWidget(context);
    }

    /**
     * 更新所有桌面的小部件
     *
     * @param context
     */
    private void updateAllWidget(Context context)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        LatestWeatherInfoManager latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_weather_layout);
        remoteViews.setImageViewResource(R.id.widget_weather_ic_im, ResultTransUtils.getWeatherIconId(latestWeatherInfoManager.getmCondition().getCode()));
        remoteViews.setTextViewText(R.id.widget_city_tv,latestWeatherInfoManager.getmCurrentCity());
        remoteViews.setTextViewText(R.id.widget_temp_tv,latestWeatherInfoManager.getmCondition().getTemp()+"°");
        manager.updateAppWidget(new ComponentName(context,WeatherInfoWidget.class.getName()),remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

}
