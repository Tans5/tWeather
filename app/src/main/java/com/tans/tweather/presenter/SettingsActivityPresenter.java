package com.tans.tweather.presenter;

import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.component.DaggerSettingsActivityComponent;
import com.tans.tweather.iviews.SettingsActivityView;
import com.tans.tweather.manager.SettingsManager;
import com.tans.tweather.module.PresenterModule;

import javax.inject.Inject;

/**
 * Created by tans on 2018/4/8.
 */

public class SettingsActivityPresenter {
    SettingsActivityView mView;

    @Inject
    SettingsManager settingsManager;

    public SettingsActivityPresenter(SettingsActivityView view) {
        mView = view;
    }

    public void injectDependences() {
        DaggerSettingsActivityComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .presenterModule(new PresenterModule(mView))
                .build()
                .inject(this);
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

    public void destroy() {
        mView = null;
    }


}
