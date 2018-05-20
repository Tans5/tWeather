package com.tans.tweather.dagger2.component;

import com.tans.tweather.activity.settings.SettingsActivity;
import com.tans.tweather.dagger2.module.PresenterModule;
import com.tans.tweather.activity.settings.presenter.SettingsActivityPresenter;
import com.tans.tweather.dagger2.scope.ActivityScope;

import dagger.Component;

/**
 * Created by tans on 2018/4/8.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface SettingsActivityComponent {
    void inject(SettingsActivity activity);
    void inject(SettingsActivityPresenter presenter);
}
