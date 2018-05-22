package com.tans.tweather.dagger2.module;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.manager.SpManager;
import com.tans.tweather.utils.NetRequestUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tans on 2018/4/7.
 */

@Module
public class ManagerModule {

    @Provides
    @Singleton
    public ChinaCitiesManager provideChinaCitiesManager(NetRequestUtils netRequestUtils,SpManager spManager) {
        ChinaCitiesManager chinaCitiesManager = ChinaCitiesManager.newInstance();
        chinaCitiesManager.initDependencies(netRequestUtils,spManager);
        return chinaCitiesManager;
    }

    @Provides
    @Singleton
    public LatestWeatherInfoManager provideLatestWeatherInfoManager(NetRequestUtils netRequestUtils,SpManager spManager) {
        LatestWeatherInfoManager latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        latestWeatherInfoManager.initDependencies(netRequestUtils,spManager);
        return latestWeatherInfoManager;
    }

    @Provides
    @Singleton
    public SpManager provideSpManager(BaseApplication context) {
        SpManager spManager = SpManager.newInstance();
        spManager.initSp(context);
        return spManager;
    }

    @Provides
    @Singleton
    public SettingsManager provideSettingsManager(SpManager spManager) {
        SettingsManager settingsManager = SettingsManager.newInstance();
        settingsManager.initDependencies(spManager);
        return  settingsManager;
    }
}
