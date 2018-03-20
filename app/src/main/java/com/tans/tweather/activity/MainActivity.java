package com.tans.tweather.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.tans.tweather.bean.AtmosphereBean;
import com.tans.tweather.bean.ConditionBean;
import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.bean.WindBean;
import com.tans.tweather.iviews.MainActivityView;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.presenter.MainActivityPresenter;
import com.tans.tweather.utils.DensityUtils;
import com.tans.tweather.utils.ResultTransUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.tans.tweather.utils.DensityUtils.dip2px;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainActivityView,DrawerLayout.DrawerListener {

    public static String TAG = MainActivity.class.getSimpleName();

    MainActivityPresenter mPresenter = null;
    @ViewById(R.id.iv_bing_bg)
    ImageView mIvBingBg;

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

    @ViewById(R.id.ll_forecast)
    LinearLayout mLlForecast;

    @ViewById(R.id.tv_title_forecast)
    TextView mTitleForecast;

    @ViewById(R.id.tv_title_condition)
    TextView mTitleCondition;

    @ViewById(R.id.tv_humidity)
    TextView mHumidity;

    @ViewById(R.id.tv_feeling_temp)
    TextView mFeelingTemp;

    @ViewById(R.id.tv_visibility)
    TextView mVisibility;

    @ViewById(R.id.iv_condition_ic)
    ImageView mConditionIc;

    @ViewById(R.id.tv_title_wind_speed)
    TextView mTitleWindSpeed;

    @ViewById(R.id.tv_wind_direction)
    TextView mWindDirection;

    @ViewById(R.id.tv_wind_speed)
    TextView mWindSpeed;

    @ViewById(R.id.tv_pressure)
    TextView mPressure;

    @ViewById(R.id.button_card)
    CardView mButtonCard;

    @ViewById(R.id.ll_error)
    LinearLayout mError;

    @ViewById(R.id.ll_main_weather_display)
    LinearLayout mMainWeatherDisplay;

    @ViewById(R.id.dl_drawer)
    DrawerLayout mDrawer;

    @ViewById(R.id.ns_scroll)
    NestedScrollView mScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        mPresenter = new MainActivityPresenter(this);
        mPresenter.loadWeatherInfo();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        updateBingBg();
        resizeView();
        mRefreshWeather.setDistanceToTriggerSync(600);
        mRefreshWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateWeather();
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,mToolbar,
                R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mRefreshWeather.setEnabled(true);
                mWeatherContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mRefreshWeather.setEnabled(false);
                mWeatherContent.setVisibility(View.GONE);
            }
        };
        actionBarDrawerToggle.syncState();
        mDrawer.addDrawerListener(actionBarDrawerToggle);
    }

    private void resizeView() {
        int screenHeight = DensityUtils.getScreenHeight(this);
        int statusBarHeight = DensityUtils.getStatusBarHeight(this);
        int navigationBarHeight = DensityUtils.getNavigationBarHeight(this);
        mWeatherView.setMinimumHeight(screenHeight - statusBarHeight);
        mCl.setPadding(0, statusBarHeight, 0, 0);
        mWeatherContent.setPadding(0, navigationBarHeight, 0, 0);
        CoordinatorLayout.LayoutParams lpFat = (CoordinatorLayout.LayoutParams) mFatAddCity.getLayoutParams();
        lpFat.setMargins(0, 0, 80, 50 + navigationBarHeight);
        LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams) mButtonCard.getLayoutParams();
        cardParams.setMargins(dip2px(getApplicationContext(),
                10),dip2px(getApplicationContext(),
                5),dip2px(getApplicationContext(),10),
                navigationBarHeight+dip2px(getApplicationContext(),10+10));
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
                insertBingBgColor(resource);
                return false;
            }
        }).into(mIvBingBg);
    }

    private void insertBingBgColor(Drawable drawable) {
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Palette.from(b).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力的
                Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();//有活力的，暗色
                Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();//有活力的，亮色
                Palette.Swatch muted = palette.getMutedSwatch();//柔和的
                Palette.Swatch mutedDark = palette.getDarkMutedSwatch();//柔和的，暗色
                Palette.Swatch mutedLight = palette.getLightMutedSwatch();//柔和的,亮色
                if (muted != null) {
                    mFatAddCity.setRippleColor(muted.getRgb());
                }
                if (vibrantLight != null && muted != null && mutedDark != null) {
                    setDrawerBg(vibrantLight.getRgb(),muted.getRgb(),muted.getRgb());
                }

                if (mutedDark != null) {
                    setWeatherTitleColor(mutedDark.getRgb());
                }

            }
        });
    }

    private void setDrawerBg(int startColor, int centerColor, int endColor) {
        int [] colors = {startColor,centerColor,endColor};
        GradientDrawable gd = new GradientDrawable();
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            gd.setOrientation(GradientDrawable.Orientation.TR_BL);
            gd.setColors(colors);
            mMenuContainer.setBackground(gd);
        } else {
            mMenuContainer.setBackgroundColor(centerColor);
        }
    }

    private void setWeatherTitleColor (int color) {
        mTitleCondition.setTextColor(color);
        mTitleForecast.setTextColor(color);
        mTitleWindSpeed.setTextColor(color);
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
        AtmosphereBean atmosphereBean = latestWeatherInfoManager.getmAtmosphere();
        WindBean windBean = latestWeatherInfoManager.getmWind();

        mIvWeatherIc.setImageDrawable(getResources().getDrawable(ResultTransUtils.getWeatherIconId(conditionBean.getCode())));
        mTvDes.setText(conditionBean.getText());
        mTvHighTemp.setText(forecastBean.get(0).getItem().getForecast().getHigh() + " °");
        mTvLowTemp.setText(forecastBean.get(0).getItem().getForecast().getLow() + " °");
        mTvTemperature.setText(conditionBean.getTemp() + " °");

        mFeelingTemp.setText(conditionBean.getTemp() + " °");
        mVisibility.setText((int)atmosphereBean.getVisibility()+"公里");
        mConditionIc.setImageDrawable(getResources().getDrawable(ResultTransUtils.getWeatherIconId(conditionBean.getCode())));
        mHumidity.setText(atmosphereBean.getHumidity()+"%");

        mWindSpeed.setText(windBean.getSpeed()+"");
        mPressure.setText((int)atmosphereBean.getPressure()+"");
        mWindDirection.setText(ResultTransUtils.getWindDirection(windBean.getDirection()));
        refreshForecast(forecastBean);
    }

    @Override
    public void setWeatherViewEnable(boolean b) {
        if(b) {
            mError.setVisibility(View.GONE);
            mMainWeatherDisplay.setVisibility(View.VISIBLE);
            mWeatherContent.setVisibility(View.VISIBLE);
        } else {
            mError.setVisibility(View.VISIBLE);
            mMainWeatherDisplay.setVisibility(View.GONE);
            mWeatherContent.setVisibility(View.GONE);
        }
    }

    private void refreshForecast(List<ForecastBean> forecastBeans) {
        boolean needInflate = true;
        if(mLlForecast.getChildCount() == 12) {
            needInflate = false;
        }
        for (int i = 0; i < forecastBeans.size(); i++) {
            View item = null;
            if (needInflate) {
                item = View.inflate(this, R.layout.forecast_weather_item, null);
            } else {
                item = mLlForecast.getChildAt(i+2);
            }
            TextView day = item.findViewById(R.id.tv_day);
            day.setText(forecastBeans.get(i).getItem().getForecast().getDay());

            ImageView ic = item.findViewById(R.id.iv_weather_ic_item);
            ic.setImageDrawable(getResources().getDrawable(ResultTransUtils.getWeatherIconId(forecastBeans.get(i).getItem().getForecast().getCode())));

            TextView tvH = item.findViewById(R.id.tv_high_temp_item);
            tvH.setText(forecastBeans.get(i).getItem().getForecast().getHigh()+ " °");

            TextView tvL = item.findViewById(R.id.tv_low_temp_item);
            tvL.setText(forecastBeans.get(i).getItem().getForecast().getLow()+ " °");
            if (forecastBeans.size() == i + 1) {
                item.findViewById(R.id.gap_line).setVisibility(View.GONE);
            }
            if(needInflate) {
                mLlForecast.addView(item);
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mRefreshWeather.setEnabled(false);
        mWeatherContent.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mRefreshWeather.setEnabled(true);
        mWeatherContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
