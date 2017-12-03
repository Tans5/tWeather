package com.tans.tweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.tans.tweather.manager.LatestWeatherInfoManager;

import java.util.Date;

/**
 * Created by tans on 2017/12/2.
 */

public class UpdateWeatherInfoService extends Service {

    private static long AN_HOUR = 1000 * 60 * 60;
    private static UpdateWeatherInfoService instance = null;
    public static String TAG = UpdateWeatherInfoService.class.getSimpleName();
    public LatestWeatherInfoManager latestWeatherInfoManager = null;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance(this);
        instance = this;
        registerReceiver(mUpdateWeatherReceiver, new IntentFilter(UPDATE_WEATHER));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on start command");
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 3*AN_HOUR;
        Intent intent1 = new Intent();
        intent1.setAction(UPDATE_WEATHER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy");
        instance = null;
        unregisterReceiver(mUpdateWeatherReceiver);
    }
}
