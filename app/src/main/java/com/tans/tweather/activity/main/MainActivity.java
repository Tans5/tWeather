package com.tans.tweather.activity.main;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tans.tweather.R;
import com.tans.tweather.activity.addcity.AddCityActivity_;
import com.tans.tweather.activity.settings.SettingsActivity_;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.bean.account.UserBean;
import com.tans.tweather.dialog.LogDialogBuilder;
import com.tans.tweather.dialog.SignUpDialogBuilder;
import com.tans.tweather.bean.weather.AtmosphereBean;
import com.tans.tweather.bean.weather.ConditionBean;
import com.tans.tweather.bean.weather.ForecastBean;
import com.tans.tweather.bean.weather.WindBean;
import com.tans.tweather.dagger2.component.DaggerMainActivityComponent;
import com.tans.tweather.mvp.view.MainActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.LatestWeatherInfoManager;
import com.tans.tweather.dagger2.module.PresenterModule;
import com.tans.tweather.activity.main.presenter.MainActivityPresenter;
import com.tans.tweather.utils.DensityUtils;
import com.tans.tweather.utils.ResponseConvertUtils;
import com.tans.tweather.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import static com.tans.tweather.utils.DensityUtils.dip2px;
import static com.tans.tweather.utils.DensityUtils.getStatusBarHeight;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainActivityView {

    public static String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainActivityPresenter mPresenter = null;

    @Inject
    ToastUtils mToastUtils;

    Bitmap mBingBitmap;

    ActionBarDrawerToggle mActionBarDrawerToggle;

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
    LinearLayout mMenuContainer;

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

    @ViewById(R.id.ns_location)
    NestedScrollView mLocationScroll;

    @ViewById(R.id.tv_current_city)
    TextView mCurrentCity;

    @ViewById(R.id.ll_location)
    LinearLayout mCommonCitiesGroup;

    @ViewById(R.id.tv_current_use_city)
    TextView mCurrentUseCity;

    @ViewById(R.id.iv_fan)
    ImageView mFan;

    @ViewById(R.id.view_scrim)
    View mScrim;

    @ViewById(R.id.tv_title)
    TextView mTitle;

    @ViewById(R.id.ll_login)
    LinearLayout mLoginLl;

    @ViewById(R.id.ll_user)
    LinearLayout mUserLl;

    @ViewById(R.id.tv_username)
    TextView mUsernameTv;

    SignUpDialogBuilder.SignUpDialog mSignUpDialog;

    LogDialogBuilder.LogDialog mLogDialog;

    Animation rotateAnim;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean isRefreshing() {
        return mRefreshWeather.isRefreshing();
    }

    @Override
    public void closeRefreshing() {
        mRefreshWeather.setRefreshing(false);
    }

    @Override
    public void startRefreshing() {
        mRefreshWeather.setRefreshing(true);
    }

    @Override
    public void refreshWeatherInfo(MainActivityPresenter.WeatherVo weatherVo) {
        LatestWeatherInfoManager latestWeatherInfoManager = LatestWeatherInfoManager.newInstance();
        ConditionBean conditionBean = weatherVo.condition;
        List<ForecastBean> forecastBean = weatherVo.forecast;
        AtmosphereBean atmosphereBean = weatherVo.atmosphere;
        WindBean windBean = weatherVo.wind;

        mIvWeatherIc.setImageDrawable(getResources().getDrawable(ResponseConvertUtils.getWeatherIconId(conditionBean.getCode())));
        mTvDes.setText(conditionBean.getText());
        mTvHighTemp.setText(forecastBean.get(0).getItem().getForecast().getHigh() + " °");
        mTvLowTemp.setText(forecastBean.get(0).getItem().getForecast().getLow() + " °");
        mTvTemperature.setText(conditionBean.getTemp() + " °");

        mFeelingTemp.setText(conditionBean.getTemp() + " °");
        mVisibility.setText((int) atmosphereBean.getVisibility() + "公里");
        mConditionIc.setImageDrawable(getResources().getDrawable(ResponseConvertUtils.getWeatherIconId(conditionBean.getCode())));
        mHumidity.setText(atmosphereBean.getHumidity() + "%");

        mWindSpeed.setText(windBean.getSpeed() + "");
        mPressure.setText((int) atmosphereBean.getPressure() + "");
        mWindDirection.setText(ResponseConvertUtils.getWindDirection(windBean.getDirection()));

        mScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View child = v.getChildAt(0);
                if (child.getHeight() == scrollY + v.getHeight()) {
                    startFanAnim();
                    Log.i(TAG, "end...............");
                } else {
                    stopFanAnim();
                }
            }
        });

        mCurrentUseCity.setVisibility(View.VISIBLE);
        mCurrentUseCity.setText(latestWeatherInfoManager.getCurrentCity());
        refreshForecast(forecastBean);
    }

    @Override
    public void setWeatherViewEnable(boolean b) {
        if (b) {
            mError.setVisibility(View.GONE);
            mMainWeatherDisplay.setVisibility(View.VISIBLE);
            mWeatherContent.setVisibility(View.VISIBLE);
        } else {
            mError.setVisibility(View.VISIBLE);
            mMainWeatherDisplay.setVisibility(View.GONE);
            mWeatherContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshMenuCites(List<String> cites, final String currentCity) {
        mCommonCitiesGroup.removeAllViews();
        if (currentCity.equals(ChinaCitiesManager.LOAD_CURRENT_LOCATION)) {
            mCurrentCity.setText("当前位置");
            mCurrentCity.setTag(ChinaCitiesManager.LOAD_CURRENT_LOCATION);
        } else {
            mCurrentCity.setText(currentCity);
            mCurrentCity.setTag(currentCity);
            final LinearLayout currentLocation = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_common_city, mCommonCitiesGroup, false);
            currentLocation.findViewById(R.id.iv_common_city_delete).setVisibility(View.GONE);
            ((TextView) currentLocation.findViewById(R.id.tv_common_city)).setText("当前位置");
            currentLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshMenuCites(mPresenter.getCommonCites(), ChinaCitiesManager.LOAD_CURRENT_LOCATION);
                    mPresenter.changeCurrentCity(ChinaCitiesManager.LOAD_CURRENT_LOCATION);
                    mDrawer.closeDrawer(Gravity.LEFT, true);
                }
            });
            mCommonCitiesGroup.addView(currentLocation);
        }
        for (final String city : cites) {
            if (!city.equals(currentCity)) {
                final LinearLayout llCity = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_common_city, mCommonCitiesGroup, false);
                llCity.setTag(city);
                final TextView tvCity = llCity.findViewById(R.id.tv_common_city);
                tvCity.setText(city);
                ImageView deleteIV = llCity.findViewById(R.id.iv_common_city_delete);
                deleteIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommonCitiesGroup.removeView(llCity);
                        mPresenter.removeCommonCity(city);
                    }
                });
                llCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentCity.setTag(city);
                        mCurrentCity.setText(city);
                        tvCity.setText(currentCity);
                        llCity.setTag(currentCity);
                        mPresenter.changeCurrentCity(city);
                        mDrawer.closeDrawer(Gravity.LEFT, true);
                    }
                });
                mCommonCitiesGroup.addView(llCity);
            }
        }
    }

    @Override
    public void refreshScrim(int alpha) {
        float a = 255 * alpha / 100;
        mScrim.setBackgroundColor(Color.argb((int) a, 0, 0, 0));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void showToast(String msg) {
        mToastUtils.showShortText(msg);
    }

    @Override
    public void signUpSuccess(UserBean userBean) {
        if(mSignUpDialog != null) {
            mSignUpDialog.dismiss();
        }
        mLoginLl.setVisibility(View.GONE);
        mUserLl.setVisibility(View.VISIBLE);
        mUsernameTv.setText(userBean.getName());

    }

    @Override
    public void signUpFail() {
        if(mSignUpDialog != null) {
            mSignUpDialog.dismiss();
        }
        mLoginLl.setVisibility(View.VISIBLE);
        mUserLl.setVisibility(View.GONE);
    }

    @Override
    public void logInSuccess(UserBean userBean) {
        if(mLogDialog != null) {
            mLogDialog.dismiss();
        }

        mLoginLl.setVisibility(View.GONE);
        mUserLl.setVisibility(View.VISIBLE);
        mUsernameTv.setText(userBean.getName());
    }

    @Override
    public void logInFail() {
        if(mLogDialog != null) {
            mLogDialog.dismiss();
        }
        mLoginLl.setVisibility(View.VISIBLE);
        mUserLl.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                    mPresenter.loadCurrentCity();
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        transStatusColor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @AfterViews
    void init() {
        DaggerMainActivityComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .presenterModule(new PresenterModule(this))
                .build()
                .inject(this);
        transStatusColor();
        startEnterTransition();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter.refreshScrim();
        if (mPresenter.loadBingImage())
            updateBingBg();
        resizeView();
        mRefreshWeather.setDistanceToTriggerSync(600);
        mRefreshWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadWeatherInfo(true);
            }
        });
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
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
        mActionBarDrawerToggle.syncState();
        mDrawer.addDrawerListener(mActionBarDrawerToggle);
    }

    @Click(R.id.fat_add_city)
    void addCity() {
        Intent intent = new Intent(this, AddCityActivity_.class);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(findViewById(R.id.tv_title), "toolbar")).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Click(R.id.ll_save_bing_image)
    void saveBingImage() {
        mDrawer.closeDrawer(Gravity.LEFT, true);
        if (mBingBitmap == null) {
            mToastUtils.showShortText("未获取到美图");
        } else {
            mPresenter.saveImage(mBingBitmap);
        }
    }

    @Click(R.id.ll_settings)
    void settings() {
        Intent intent = new Intent(this, SettingsActivity_.class);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Click(R.id.ll_login)
    void logIn() {
        mLogDialog = new LogDialogBuilder()
                .setListener(new LogDialogBuilder.LogDialogListener() {
                    @Override
                    public void log(String name, String password, LogDialogBuilder.LogDialog dialog) {
                        dialog.showWaiting();
                        mPresenter.logIn(name,password);
                    }

                    @Override
                    public void cancel(LogDialogBuilder.LogDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void signUp(LogDialogBuilder.LogDialog dialog) {
                        dialog.dismiss();
                        createSignUpDialog();
                    }
                })
                .build();
        mLogDialog.show(getFragmentManager(),"log_in");
    }

    @Click(R.id.ll_user)
    void logOut() {
        mPresenter.logOut();
        mLoginLl.setVisibility(View.VISIBLE);
        mUserLl.setVisibility(View.GONE);
    }

    private void createSignUpDialog() {
        mSignUpDialog = new SignUpDialogBuilder()
                .setListener(new SignUpDialogBuilder.SignUpListener() {
                    @Override
                    public void signUp(String name, String password, String passwordRepeat, SignUpDialogBuilder.SignUpDialog dialog) {
                        if(TextUtils.equals(password,passwordRepeat)) {
                            dialog.showWaiting();
                            mPresenter.signUp(name,password);
                        } else {
                            showToast("两次密码不一致哦～");
                        }
                    }

                    @Override
                    public void reset(SignUpDialogBuilder.SignUpDialog dialog) {

                    }

                    @Override
                    public void cancel(SignUpDialogBuilder.SignUpDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        mSignUpDialog.show(getFragmentManager(),"sign_up");
    }

    private void startEnterTransition() {
        if (Build.VERSION.SDK_INT >= 21) {
            Transition transition = new Fade();
            transition.setDuration(500);
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    mPresenter.loadWeatherInfo(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.removeListener(this);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            getWindow().setEnterTransition(transition);
            //   getWindow().setReturnTransition(new Fade());
            //   getWindow().setSharedElementEnterTransition(transition);
        } else {
            mPresenter.loadWeatherInfo(false);
        }
    }

    private void resizeView() {
        int screenHeight = DensityUtils.getScreenHeight(this);
        int statusBarHeight = DensityUtils.getStatusBarHeight(this);
        int navigationBarHeight = DensityUtils.getNavigationBarHeight(this);
        if (!DensityUtils.checkDeviceHasNavigationBar(this)) {
            navigationBarHeight = 0;
        }
        if (Build.VERSION.SDK_INT < 21) {
            statusBarHeight = 0;
        }
        mWeatherView.setMinimumHeight(screenHeight - statusBarHeight);
        mCl.setPadding(0, statusBarHeight, 0, 0);
        mWeatherContent.setPadding(0, navigationBarHeight, 0, 0);
        CoordinatorLayout.LayoutParams lpFat = (CoordinatorLayout.LayoutParams) mFatAddCity.getLayoutParams();
        lpFat.setMargins(0, 0, 80, 50 + navigationBarHeight);
        LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams) mButtonCard.getLayoutParams();
        cardParams.setMargins(dip2px(getApplicationContext(),
                10), dip2px(getApplicationContext(),
                5), dip2px(getApplicationContext(), 10),
                navigationBarHeight + dip2px(getApplicationContext(), 10 + 10));
        LinearLayout.LayoutParams lpScrollLocation = (LinearLayout.LayoutParams) mLocationScroll.getLayoutParams();
        lpScrollLocation.setMargins(0, getStatusBarHeight(this), 0, 0);

        ViewGroup.LayoutParams mMenuParamas = mMenuContainer.getLayoutParams();
        mMenuParamas.width = DensityUtils.getScreenWith(this) * 2 / 3;
    }


    private void updateBingBg() {
        RequestOptions mOptions = new RequestOptions();
        mOptions.centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .optionalCenterCrop()
                .placeholder(R.drawable.default_bing)
        ;
        Glide.with(BaseApplication.getInstance()).load("http://api.dujin.org/bing/1366.php")
                .apply(mOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        insertBingBgColor(resource);
                        return false;
                    }
                })
                .transition(new DrawableTransitionOptions().crossFade(1000))
                .into(mIvBingBg);
    }

    private void insertBingBgColor(Drawable resource) {
        Bitmap b = ((BitmapDrawable) resource).getBitmap();
        mBingBitmap = b;
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
                    setDrawerBg(vibrantLight.getRgb(), muted.getRgb(), muted.getRgb());
                }

                if (mutedDark != null) {
                    setWeatherTitleColor(mutedDark.getRgb());
                }
                if(vibrantLight != null) {
                   // mTitle.setTextColor(vibrantLight.getRgb());
                }

            }
        });
    }

    private void setDrawerBg(int startColor, int centerColor, int endColor) {
        int[] colors = {startColor, centerColor, endColor};
        GradientDrawable gd = new GradientDrawable();
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            gd.setOrientation(GradientDrawable.Orientation.TR_BL);
            gd.setColors(colors);
            mMenuContainer.setBackground(gd);
        } else {
            mMenuContainer.setBackgroundColor(centerColor);
        }
    }

    private void setWeatherTitleColor(int color) {
        mTitleCondition.setTextColor(color);
        mTitleForecast.setTextColor(color);
        mTitleWindSpeed.setTextColor(color);
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

    private void startFanAnim() {
        if (rotateAnim == null)
            rotateAnim = new RotateAnimation(0f, 359f, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(1500);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(RotateAnimation.INFINITE);
        if (mFan.getVisibility() == View.VISIBLE)
            mFan.startAnimation(rotateAnim);
    }

    private void stopFanAnim() {
        mFan.clearAnimation();
    }

    private void refreshForecast(List<ForecastBean> forecastBeans) {
        boolean needInflate = true;
        if (mLlForecast.getChildCount() == 12) {
            needInflate = false;
        }
        for (int i = 0; i < forecastBeans.size(); i++) {
            View item = null;
            if (needInflate) {
                item = View.inflate(this, R.layout.item_forecast_weather, null);
            } else {
                item = mLlForecast.getChildAt(i + 2);
            }
            TextView day = item.findViewById(R.id.tv_day);
            day.setText(forecastBeans.get(i).getItem().getForecast().getDay());

            ImageView ic = item.findViewById(R.id.iv_weather_ic_item);
            ic.setImageDrawable(getResources().getDrawable(ResponseConvertUtils.getWeatherIconId(forecastBeans.get(i).getItem().getForecast().getCode())));

            TextView tvH = item.findViewById(R.id.tv_high_temp_item);
            tvH.setText(forecastBeans.get(i).getItem().getForecast().getHigh() + " °");

            TextView tvL = item.findViewById(R.id.tv_low_temp_item);
            tvL.setText(forecastBeans.get(i).getItem().getForecast().getLow() + " °");
            if (forecastBeans.size() == i + 1) {
                item.findViewById(R.id.gap_line).setVisibility(View.GONE);
            }
            if (needInflate) {
                mLlForecast.addView(item);
            }
        }
    }


}
