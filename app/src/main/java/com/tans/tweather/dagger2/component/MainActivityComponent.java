package com.tans.tweather.dagger2.component;

import com.tans.tweather.activity.main.MainActivity;
import com.tans.tweather.dagger2.module.PresenterModule;
import com.tans.tweather.activity.main.presenter.MainActivityPresenter;
import com.tans.tweather.dagger2.scope.ActivityScope;

import dagger.Component;

/**
 * Created by tans on 2018/4/7.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class,modules = PresenterModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(MainActivityPresenter mainActivityPresenter);
}
