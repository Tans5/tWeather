package com.tans.tweather.dagger2.component;

import com.tans.tweather.dagger2.scope.ReceiverScope;
import com.tans.tweather.widget.WeatherInfoWidget;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */

@ReceiverScope
@Component(dependencies = ApplicationComponent.class)
public interface WidgetComponent {
    void inject(WeatherInfoWidget widget);
}
