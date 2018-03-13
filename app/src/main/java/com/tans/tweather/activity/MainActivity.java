package com.tans.tweather.activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tans.tweather.R;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.presenter.MainActivityPresenter;
import com.tans.tweather.utils.ResultTransUtils;
import com.tans.tweather.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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

    @ViewById(R.id.fat_add_city)
    FloatingActionButton mFatAddCity;

    @ViewById(R.id.abl)
    AppBarLayout mAppBar;

    @ViewById(R.id.tool_bar)
    Toolbar mToolbar;

    @ViewById(R.id.iv_weather_ic)
    ImageView mIvWeatherIc;

    @ViewById(R.id.tv_description)
    TextView mTvDes;

    @ViewById(R.id.tv_high_temp)
    TextView mTvHighTemp;

    @ViewById(R.id.tv_low_temp)
    TextView mTvLowTemp;

    @ViewById(R.id.tv_temperature)
    TextView mTvTemperature;

    @ViewById(R.id.rl_menu_container)
    RelativeLayout mMenuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init () {
        mPresenter = new MainActivityPresenter(this);
        mPresenter.loadWeatherInfo();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateBingBg();
        resizeView();
        mRefreshWeather.setDistanceToTriggerSync(1000);
        mRefreshWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateWeather();
            }
        });
    }

    private void resizeView() {
        mWeatherView.setMinimumHeight(getScreenHeight(this) - getStatusBarHeight(this));
        mCl.setPadding(0,getStatusBarHeight(this),0,0);
        mWeatherContent.setPadding(0,getNavigationBarHeight(this),0,0);
        CoordinatorLayout.LayoutParams lpFat = (CoordinatorLayout.LayoutParams) mFatAddCity.getLayoutParams();
        lpFat.setMargins(0,0,80,50+getNavigationBarHeight(this));
    }


    private void updateBingBg() {
        transStatusColor();
        RequestOptions mOptions = new RequestOptions();
        mOptions.centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load("http://api.dujin.org/bing/1366.php").apply(mOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                initBingBgColor(resource);
                return false;
            }
        }).into(mIvBingBg);
    }

    private void initBingBgColor(Drawable drawable) {
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Palette.from(b).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力的
                Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();//有活力的，暗色
                Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();//有活力的，亮色
                Palette.Swatch muted = palette.getMutedSwatch();//柔和的
                Palette.Swatch mutedDark = palette.getDarkMutedSwatch();//柔和的，暗色
                Palette.Swatch mutedLight = palette.getLightMutedSwatch();//柔和的,亮色
                if (muted !=null) {
                    mFatAddCity.setRippleColor(muted.getRgb());
                }
                if (vibrantLight != null) {
                    mMenuContainer.setBackgroundColor(vibrantLight.getRgb());
                }
            }
        });
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

    @Override
    public boolean isRefreshing() {
        return mRefreshWeather.isRefreshing();
    }

    @Override
    public void closeRefreshing() {
        mRefreshWeather.setRefreshing(false);
    }

    @Click(R.id.fat_add_city)
    void addCity() {

    }

    @Override
    public void refreshWeatherInfo() {
        LatestWeatherInfoManager latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        ConditionBean conditionBean = latestWeatherInfoManager.getmCondition();
        List<ForecastBean> forecastBean = latestWeatherInfoManager.getmForecast();
        mIvWeatherIc.setImageDrawable(getResources().getDrawable(ResultTransUtils.getWeatherIconId(conditionBean.getCode())));
        mTvDes.setText(conditionBean.getText());
        mTvHighTemp.setText(forecastBean.get(0).getItem().getForecast().getHigh()+" °");
        mTvLowTemp.setText(forecastBean.get(0).getItem().getForecast().getLow()+" °");
        mTvTemperature.setText(conditionBean.getTemp()+" °");
    }
}
