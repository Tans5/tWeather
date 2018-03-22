package com.tans.tweather.iviews;

import android.animation.Animator;

import com.tans.tweather.adapter.CitiesRecyclerAdapter;

/**
 * Created by mine on 2018/3/20.
 */

public interface AddCityActivityView {
    void initRecyclerView(CitiesRecyclerAdapter adapter);
    void setLoadingViewEnable(boolean b);
    void startMoveReveal(Animator.AnimatorListener listener);
    void startBackReveal(Animator.AnimatorListener listener);
}
