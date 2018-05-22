package com.tans.tweather.dagger2.component;

import com.tans.tweather.dagger2.scope.ServiceScope;
import com.tans.tweather.service.UpdateWeatherInfoService;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ServiceScope
@Component(dependencies = ApplicationComponent.class)
public interface UpdateServiceComponent {
    void inject(UpdateWeatherInfoService service);
}
