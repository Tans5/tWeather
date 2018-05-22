package com.tans.tweather.dagger2.component;


import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.manager.SpManager;
import com.tans.tweather.dagger2.module.ApplicationModule;
import com.tans.tweather.dagger2.module.ManagerModule;
import com.tans.tweather.dagger2.module.UtilsModule;
import com.tans.tweather.utils.NetRequestUtils;
import com.tans.tweather.utils.ToastUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ManagerModule.class, UtilsModule.class})
public interface ApplicationComponent {
    BaseApplication baseApplication();
    ChinaCitiesManager chinaCitiesManager();
    LatestWeatherInfoManager latestWeatherInfoManager();
    SpManager spManager();
    NetRequestUtils netRequestUtils();
    ToastUtils toastUtils();
    SettingsManager settingsManager();
}
