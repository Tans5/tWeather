package com.tans.tweather.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.tans.tweather.R;
import com.tans.tweather.application.BaseApplication;

/**
 * Created by mine on 2018/3/9.
 */

public class FatBehavior extends FloatingActionButton.Behavior {


    public FatBehavior(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public boolean isAnimate = false;

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE && !isAnimate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                child.animate().translationY(200)
                        .alpha(0f)
                        .setDuration(300)
                        .setInterpolator(new FastOutLinearInInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimate = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimate = false;
                                child.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();
            }
        } else if (dyConsumed < 0 && child.getVisibility() == View.INVISIBLE && !isAnimate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                child.animate().translationY(0f)
                        .alpha(1f)
                        .setDuration(300)
                        .setInterpolator(new FastOutLinearInInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                child.setVisibility(View.VISIBLE);
                                isAnimate = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimate = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();

            }
        }
    }

}
