package com.tans.tweather.application;

import android.app.Application;

import com.tans.tweather.dagger2.component.ApplicationComponent;
import com.tans.tweather.dagger2.component.DaggerApplicationComponent;
import com.tans.tweather.dagger2.module.ApplicationModule;
import com.tans.tweather.dagger2.module.ManagerModule;
import com.tans.tweather.dagger2.module.UtilsModule;

/**
 * Created by mine on 2017/12/28.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance = null;
    private static ApplicationComponent applicationComponent;
    public static BaseApplication getInstance() {
        return instance;
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .managerModule(new ManagerModule())
                .utilsModule(new UtilsModule())
                .build();
    }
}
