package com.tans.tweather.activity.settings.presenter;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.dagger2.component.DaggerSettingsActivityComponent;
import com.tans.tweather.mvp.Presenter;
import com.tans.tweather.mvp.view.SettingsActivityView;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.dagger2.module.PresenterModule;

import javax.inject.Inject;

/**
 * Created by tans on 2018/4/8.
 */

public class SettingsActivityPresenter implements Presenter {
    SettingsActivityView mView;

    @Inject
    SettingsManager settingsManager;

    public SettingsActivityPresenter(SettingsActivityView view) {
        mView = view;
    }

    @Override
    public void initDependencies() {
        DaggerSettingsActivityComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .presenterModule(new PresenterModule(mView))
                .build()
                .inject(this);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    public void refreshData() {
        mView.refreshViews(settingsManager.isOpenService(),settingsManager.isLoadImage()
                ,settingsManager.isOpenNotification(),settingsManager.getRate(),settingsManager.getAlpha());
    }

    public void save(boolean openService,boolean loadImage,boolean openNotification,int rate,int alpha) {
        settingsManager.setOpenService(openService);
        settingsManager.setLoadImage(loadImage);
        settingsManager.setRate(rate);
        settingsManager.setAlpha(alpha);
        settingsManager.setOpenNotification(openNotification);
        settingsManager.save();
    }
}
