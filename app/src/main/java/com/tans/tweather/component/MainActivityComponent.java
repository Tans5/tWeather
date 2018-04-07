package com.tans.tweather.component;

import com.tans.tweather.activity.MainActivity;
import com.tans.tweather.module.ApplicationModule;
import com.tans.tweather.module.PresenterModule;
import com.tans.tweather.presenter.MainActivityPresenter;
import com.tans.tweather.scrop.ActivityScrop;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ActivityScrop
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(MainActivityPresenter mainActivityPresenter);
}
