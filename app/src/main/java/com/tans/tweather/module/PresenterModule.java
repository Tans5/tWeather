package com.tans.tweather.module;

import com.tans.tweather.activity.AddCityActivity;
import com.tans.tweather.iviews.AddCityActivityView;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.iviews.View;
import com.tans.tweather.presenter.AddCityActivityPresenter;
import com.tans.tweather.presenter.MainActivityPresenter;
import com.tans.tweather.scrop.ActivityScrop;

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

    @ActivityScrop
    @Provides
    public MainActivityPresenter provideMainActivityPresenter() {
        return new MainActivityPresenter((MainActivityView) mView);
    }

    @ActivityScrop
    @Provides
    public AddCityActivityPresenter provideAddCityActivityPresenter() {
        return  new AddCityActivityPresenter((AddCityActivityView) mView);
    }
}
