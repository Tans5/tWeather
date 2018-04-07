package com.tans.tweather.component;

import com.tans.tweather.activity.AddCityActivity;
import com.tans.tweather.module.ApplicationModule;
import com.tans.tweather.module.PresenterModule;
import com.tans.tweather.presenter.AddCityActivityPresenter;
import com.tans.tweather.scrop.ActivityScrop;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ActivityScrop
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface AddCityActivityComponent {
    void inject(AddCityActivity addCityActivity);
    void inject(AddCityActivityPresenter addCityActivityPresenter);
}
