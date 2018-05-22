package com.tans.tweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.tans.tweather.R;
import com.tans.tweather.activity.welcome.WelcomeActivity;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.dagger2.component.DaggerUpdateServiceComponent;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.utils.ResponseConvertUtils;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by tans on 2017/12/2.
 */

public class UpdateWeatherInfoService extends Service {

    private static long AN_HOUR = 1000 * 60 * 60;
    private static UpdateWeatherInfoService instance = null;
    private static int NOTIFICATION_IDENTIFY_CODE = 1;

    public static String TAG = UpdateWeatherInfoService.class.getSimpleName();
    @Inject
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    @Inject
    ChinaCitiesManager chinaCitiesManager = null;
    @Inject
    SettingsManager settingsManager = null;
    public static UpdateWeatherInfoService getInstance() {
        return instance;
    }

    private static String UPDATE_WEATHER = "UPDATE_WEATHER";
    private BroadcastReceiver mUpdateWeatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "request weather update " + new Date().getSeconds());
            latestWeatherInfoManager.updateLatestWeatherInfo();
            Intent intent1 = new Intent(instance, UpdateWeatherInfoService.class);
            startService(intent1);
        }
    };

    private LatestWeatherInfoManager.WeatherUpdatedListener weatherUpdatedListener = new LatestWeatherInfoManager.WeatherUpdatedListener() {
        @Override
        public void updated() {
            if(settingsManager.isOpenNotification()) {
                showNotification();
            }
        }
    };

    private SettingsManager.SettingsChangeListener settingsChangeListener = new SettingsManager.SettingsChangeListener() {
        @Override
        public void settingsChange() {
            if(settingsManager.isOpenNotification()) {
                showNotification();
            } else {
                cancelNotification();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerUpdateServiceComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this);
        if(latestWeatherInfoManager.getCurrentCity().equals("")) {
            if(chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
                chinaCitiesManager.loadCurrentCity(new ChinaCitiesManager.LoadCurrentCityListener() {
                    @Override
                    public void onSuccess(String s) {
                        latestWeatherInfoManager.setCurrentCity(s);
                    }

                    @Override
                    public void onFail(String e) {
                        Log.i(TAG,"请求位置信息失败");
                    }
                });
            } else {
                latestWeatherInfoManager.setCurrentCity(chinaCitiesManager.getCurrentCity());
            }
        }
        instance = this;
        registerReceiver(mUpdateWeatherReceiver, new IntentFilter(UPDATE_WEATHER));
        latestWeatherInfoManager.registerWeatherUpdateListener(weatherUpdatedListener);
        settingsManager.registerListener(settingsChangeListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on start command");
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + AN_HOUR*settingsManager.getRate();
        Intent intent1 = new Intent();
        intent1.setAction(UPDATE_WEATHER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroyActivity");
        instance = null;
        unregisterReceiver(mUpdateWeatherReceiver);
        latestWeatherInfoManager.unregisterWeatherUpdateListener(weatherUpdatedListener);
        settingsManager.unregisterListener(settingsChangeListener);
    }

    private void showNotification() {
        RemoteViews v = new RemoteViews(this.getPackageName(), R.layout.layout_notification_weather);
        v.setImageViewResource(R.id.iv_weather_ic,
                ResponseConvertUtils.getWeatherIconId(latestWeatherInfoManager.getCondition().getCode()));
        v.setTextViewText(R.id.tv_temperature,latestWeatherInfoManager.getCondition().getTemp()+"°");
        v.setTextViewText(R.id.tv_city,latestWeatherInfoManager.getCurrentCity());

        Intent intent = new Intent(this, WelcomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContent(v);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setSmallIcon(ResponseConvertUtils.getWeatherIconId(latestWeatherInfoManager.getCondition().getCode()));
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        //notification.visibility = Notification.VISIBILITY_SECRET;
        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_IDENTIFY_CODE,notification);
    }

    public void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_IDENTIFY_CODE);
    }
}
