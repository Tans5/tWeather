package com.tans.tweather.component;

import com.tans.tweather.scrop.ReciverScrop;
import com.tans.tweather.widget.WeatherInfoWidget;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */

@ReciverScrop
@Component(dependencies = ApplicationComponent.class)
public interface WidgetComponent {
    void inject(WeatherInfoWidget widget);
}
