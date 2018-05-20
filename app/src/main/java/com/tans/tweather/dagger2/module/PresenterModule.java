package com.tans.tweather.dagger2.module;

import com.tans.tweather.mvp.view.AddCityActivityView;
import com.tans.tweather.mvp.view.MainActivityView;
import com.tans.tweather.mvp.view.SettingsActivityView;
import com.tans.tweather.mvp.View;
import com.tans.tweather.activity.addcity.presenter.AddCityActivityPresenter;
import com.tans.tweather.activity.main.presenter.MainActivityPresenter;
import com.tans.tweather.activity.settings.presenter.SettingsActivityPresenter;
import com.tans.tweather.dagger2.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tans on 2018/4/7.
 */
@Module
public class PresenterModule {
    private View mView;

    public PresenterModule(View view) {
        this.mView = view;
    }

    @ActivityScope
    @Provides
    public MainActivityPresenter provideMainActivityPresenter() {
        MainActivityPresenter presenter = new MainActivityPresenter((MainActivityView) mView);
        presenter.initDependencies();
        return presenter;
    }

    @ActivityScope
    @Provides
    public AddCityActivityPresenter provideAddCityActivityPresenter() {
        AddCityActivityPresenter presenter = new AddCityActivityPresenter((AddCityActivityView) mView);
        presenter.initDependencies();
        return  presenter;
    }

    @ActivityScope
    @Provides
    public SettingsActivityPresenter provideSettingsActivityPresenter() {
        SettingsActivityPresenter presenter = new SettingsActivityPresenter((SettingsActivityView)mView);
        presenter.initDependencies();
        return presenter;
    }
}
