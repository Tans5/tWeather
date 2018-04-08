package com.tans.tweather.component;

import com.tans.tweather.activity.SettingsActivity;
import com.tans.tweather.module.PresenterModule;
import com.tans.tweather.presenter.SettingsActivityPresenter;
import com.tans.tweather.scrop.ActivityScrop;

import dagger.Component;

/**
 * Created by tans on 2018/4/8.
 */
@ActivityScrop
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface SettingsActivityComponent {
    void inject(SettingsActivity activity);
    void inject(SettingsActivityPresenter presenter);
}
