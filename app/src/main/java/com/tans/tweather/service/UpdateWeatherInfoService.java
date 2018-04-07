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

import com.android.volley.VolleyError;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.component.DaggerUpdateServiceComponent;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SpManager;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by tans on 2017/12/2.
 */

public class UpdateWeatherInfoService extends Service {

    private static long AN_HOUR = 1000 * 60 * 60;
    private static UpdateWeatherInfoService instance = null;
    public static String TAG = UpdateWeatherInfoService.class.getSimpleName();
    @Inject
    LatestWeatherInfoManager latestWeatherInfoManager = null;
    @Inject
    ChinaCitiesManager chinaCitiesManager = null;
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
//        latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
//        chinaCitiesManager = ChinaCitiesManager.newInstance();
        DaggerUpdateServiceComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this);
        if(latestWeatherInfoManager.getmCurrentCity().equals("")) {
            if(chinaCitiesManager.getCurrentCity().equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
                chinaCitiesManager.loadCurrentCity(new ChinaCitiesManager.LoadCurrentCityListener() {
                    @Override
                    public void onSuccess(String s) {
                        latestWeatherInfoManager.setmCurrentCity(s);
                    }

                    @Override
                    public void onFail(VolleyError e) {
                        Log.i(TAG,"请求位置信息失败");
                    }
                });
            } else {
                latestWeatherInfoManager.setmCurrentCity(chinaCitiesManager.getCurrentCity());
            }
        }
        instance = this;
        registerReceiver(mUpdateWeatherReceiver, new IntentFilter(UPDATE_WEATHER));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on start command");
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + AN_HOUR;
        Intent intent1 = new Intent();
        intent1.setAction(UPDATE_WEATHER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy");
        instance = null;
        unregisterReceiver(mUpdateWeatherReceiver);
    }
}
