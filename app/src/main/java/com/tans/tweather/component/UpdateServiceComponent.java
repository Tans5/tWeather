package com.tans.tweather.component;

import com.tans.tweather.module.ApplicationModule;
import com.tans.tweather.scrop.ServiceScrop;
import com.tans.tweather.service.UpdateWeatherInfoService;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ServiceScrop
@Component(dependencies = ApplicationComponent.class)
public interface UpdateServiceComponent {
    void inject(UpdateWeatherInfoService service);
}
