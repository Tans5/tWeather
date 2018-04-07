package com.tans.tweather.module;

import com.tans.tweather.application.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tans on 2018/4/7.
 */
@Module
public class ApplicationModule {
    @Provides
    @Singleton
    public BaseApplication provideApplication() {
        return BaseApplication.getInstance();
    }
}
