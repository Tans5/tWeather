package com.tans.tweather.application;

import android.app.Application;

/**
 * Created by mine on 2017/12/28.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance = null;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
