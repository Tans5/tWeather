package com.tans.tweather.activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tans.tweather.R;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.presenter.MainActivityPresenter;
import com.tans.tweather.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainActivityView {

    public static String TAG = MainActivity.class.getSimpleName();

    MainActivityPresenter mPresenter = null;
    @ViewById(R.id.iv_bing_bg)
    ImageView mIvBingBg ;

    @ViewById(R.id.cl)
    CoordinatorLayout mCl;

    @ViewById(R.id.ll_weather_content)
    LinearLayout mWeatherContent;

    @ViewById(R.id.weather_view)
    FrameLayout mWeatherView;

    @ViewById(R.id.refresh_weather)
    SwipeRefreshLayout mRefreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void Init () {
        mPresenter = new MainActivityPresenter(this);
        mPresenter.loadWeatherInfo();
        updateBingBg();
        resizeView();
        mRefreshWeather.setDistanceToTriggerSync(1000);
        mRefreshWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadWeatherInfo();
            }
        });
    }

    private void resizeView() {
        mWeatherView.setMinimumHeight(getScreenHeight(this) - getStatusBarHeight(this));
        mCl.setPadding(0,getStatusBarHeight(this),0,0);
        mWeatherContent.setPadding(0,getNavigationBarHeight(this),0,0);
    }


    private void updateBingBg() {
        RequestOptions mOptions = new RequestOptions();
        mOptions.centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load("http://api.dujin.org/bing/1366.php").apply(mOptions).into(mIvBingBg);
    }

    public void showLog(String s) {
        Log.i(TAG, s + "666666666666666666666");
        ToastUtils.getInstance().showShortText(s);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        transStatusColor();
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

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    private int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
