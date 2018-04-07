package com.tans.tweather.module;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.utils.NetRequestUtils;
import com.tans.tweather.utils.ToastUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tans on 2018/4/7.
 */

@Module
public class UtilsModule {

    @Provides
    @Singleton
    public NetRequestUtils provideNetRequestUtils(BaseApplication context) {
        NetRequestUtils netRequestUtils = NetRequestUtils.newInstance();
        netRequestUtils.setContext(context);
        return netRequestUtils;
    }

    @Provides
    @Singleton
    public ToastUtils provideToastUtils() {
        return ToastUtils.getInstance();
    }
}
