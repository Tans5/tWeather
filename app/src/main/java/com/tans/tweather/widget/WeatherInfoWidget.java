package com.tans.tweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by tans on 2017/12/3.
 */

public class WeatherInfoWidget extends AppWidgetProvider {

    public static String CLICK_FOR_UPDATE = "com.tans.tweather.CLICK_FOR_UPDATE";
    public static String UPDATE_WEATHER = "com.tans.tweather.UPDATE_WEATHER";
    public static String TAG = WeatherInfoWidget.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context,WeatherInfoWidget.class.getName()));
        for(int id:appWidgetIds)
            Log.i(TAG,"id:"+id);
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
