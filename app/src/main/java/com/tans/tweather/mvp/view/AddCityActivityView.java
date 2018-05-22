package com.tans.tweather.mvp.view;

import android.animation.Animator;

import com.tans.tweather.activity.addcity.adapter.CitiesRecyclerAdapter;
import com.tans.tweather.mvp.View;

/**
 * Created by mine on 2018/3/20.
 */

public interface AddCityActivityView extends View {
    void initRecyclerView(CitiesRecyclerAdapter adapter);
    void setLoadingViewEnable(boolean b);
    void startMoveReveal(Animator.AnimatorListener listener);
    void startBackReveal(Animator.AnimatorListener listener);
    void destroyActivity();
    void refreshParentCity(String city);
    void hideParentCity();
}
