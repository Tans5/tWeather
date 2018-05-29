package com.tans.tweather.activity.welcome;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.tans.tweather.R;
import com.tans.tweather.activity.main.MainActivity_;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mine on 2018/3/20.
 */

public class WelcomeActivity extends AppCompatActivity {

    String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= 21) {
//            (1)setExitTransition() - 当A startB时，使A中的View退出场景的transition    在A中设置
//
//            (2)setEnterTransition() - 当A startB时，使B中的View进入场景的transition    在B中设置
//
//            (3)setReturnTransition() - 当B 返回A时，使B中的View退出场景的transition  在B中设置
//
//            (4)setReenterTransition() - 当B 返回A时，使A中的View进入场景的transition   在A中设置
//
//            注意：
//
//            1、要在Activity B 中设置以下代码
//
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//
//            并且这行代码要加在super.onCreate(savedInstanceState);之前（这是书中没说的）
//
//            2、退场的效果目前观察只能通过返回键触发，否则没有退场效果，
//
//            例如通过finish()退出B回到A是没有动画效果的
//
//            3、4种效果设置的Activity的是不同的！！！

            Transition transition = new Fade();
            transition.setDuration(500);
            getWindow().setEnterTransition(transition);
            //getWindow().setReenterTransition(transition);
            //   getWindow().setReenterTransition(transition);
        }
        transStatusColor();
        Observable.just(260)
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        try {
                            Thread.sleep(integer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        changeActivity();
                    }
                });
    }

    private void transStatusColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
        Observable.just(1000)
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        try {
                            Thread.sleep(integer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        finish();
                        overridePendingTransition(R.anim.anim_acitivity_enter,
                                R.anim.anim_activity_exit);
                    }
                });

    }

    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(findViewById(R.id.tv_app_name), "toolbar")).toBundle());
            //    finish();
        } else {
            startActivity(intent);
            overridePendingTransition(R.anim.anim_acitivity_enter, R.anim.anim_activity_exit);
        }
    }
}
