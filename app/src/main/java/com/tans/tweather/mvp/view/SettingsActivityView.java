package com.tans.tweather.mvp.view;

import com.tans.tweather.mvp.View;

/**
 * Created by tans on 2018/4/8.
 */

public interface SettingsActivityView extends View {
    void refreshViews(boolean openService,boolean loadImage,boolean openNotification,int updateRate,int alpha);
}
