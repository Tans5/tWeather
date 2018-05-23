package com.tans.tweather.dagger2.module;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.utils.NetRequestUtils;
import com.tans.tweather.utils.ToastUtils;
import com.tans.tweather.utils.httprequest.BaseHttpRequestUtils;
import com.tans.tweather.utils.httprequest.RetrofitHttpRequestUtils;
import com.tans.tweather.utils.httprequest.VolleyHttpRequestUtils;

import javax.inject.Named;
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
    @Named("volley")
    public BaseHttpRequestUtils provideVolleyHttpRequestUtils() {
        BaseHttpRequestUtils result = VolleyHttpRequestUtils.newInstance();
        return result;
    }

    @Provides
    @Singleton
    @Named("retrofit")
    public BaseHttpRequestUtils privodeRetrofitHttpRequestUtils() {
        BaseHttpRequestUtils result = RetrofitHttpRequestUtils.newInstance();
        return result;
    }

    @Provides
    @Singleton
    public ToastUtils provideToastUtils() {
        return ToastUtils.getInstance();
    }
}
