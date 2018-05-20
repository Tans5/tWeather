package com.tans.tweather.dagger2.component;

import com.tans.tweather.activity.addcity.AddCityActivity;
import com.tans.tweather.dagger2.module.PresenterModule;
import com.tans.tweather.activity.addcity.presenter.AddCityActivityPresenter;
import com.tans.tweather.dagger2.scope.ActivityScope;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface AddCityActivityComponent {
    void inject(AddCityActivity addCityActivity);
    void inject(AddCityActivityPresenter addCityActivityPresenter);
}
