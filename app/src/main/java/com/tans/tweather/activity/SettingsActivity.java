package com.tans.tweather.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.tans.tweather.R;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.component.DaggerSettingsActivityComponent;
import com.tans.tweather.iviews.SettingsActivityView;
import com.tans.tweather.module.PresenterModule;
import com.tans.tweather.presenter.SettingsActivityPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by tans on 2018/4/7.
 */

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity implements SettingsActivityView,View.OnClickListener {

    @ViewById(R.id.tool_bar)
    Toolbar mToolbar;

    @ViewById(R.id.sb_alpha)
    SeekBar mAlphaSeekBar;

    @ViewById(R.id.tv_alpha)
    TextView mAlpha;

    @ViewById(R.id.rl_rate)
    RelativeLayout mRlRate;

    @ViewById(R.id.tv_rate)
    TextView mRate;

    @ViewById(R.id.switch_image)
    Switch mSwitchImage;

    @ViewById(R.id.switch_service)
    Switch mSwitchService;

    @ViewById(R.id.switch_notification)
    Switch mNotification;

    @ViewById(R.id.tv_rate_title)
    TextView mRateTitle;

    PopupWindow rateWindow;

    @Inject
    SettingsActivityPresenter mPresenter;

    @AfterViews
    void init() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerSettingsActivityComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .presenterModule(new PresenterModule(this))
                .build()
                .inject(this);
        mPresenter.injectDependences();
        mPresenter.refreshData();
        mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Click(R.id.switch_image)
    void switchImageClick() {

    }

    @Click(R.id.switch_service)
    void switchServiceClick() {
        if(!mSwitchService.isChecked()) {
            mRlRate.setEnabled(false);
            mRateTitle.setTextColor(getResources().getColor(R.color.colorTextGray));
        } else {
            mRlRate.setEnabled(true);
            mRateTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Click(R.id.bt_reset)
    void resetClick() {
        mPresenter.refreshData();
    }

    @Click(R.id.bt_save)
    void saveClick() {
        mPresenter.save(mSwitchService.isChecked(), mSwitchImage.isChecked(), mNotification.isChecked(),
                (int) mRate.getTag(), mAlphaSeekBar.getProgress());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }.execute();
    }

    @Click(R.id.rl_rate)
    void rateClick() {
        if (rateWindow == null)
            initPopuWindow();
        rateWindow.showAsDropDown(mRlRate);
    }

    private void initPopuWindow() {
        rateWindow = new PopupWindow(this);
        rateWindow.setWidth(mRlRate.getWidth());
        rateWindow.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);
        rateWindow.setOutsideTouchable(true);
        rateWindow.setTouchable(true);
        rateWindow.setBackgroundDrawable(null);
        if (Build.VERSION.SDK_INT >= 21) {
            rateWindow.setElevation(30f);
        }
        View v = View.inflate(this, R.layout.layout_settings_rate, null);
        v.findViewById(R.id.tv_1_hour).setOnClickListener(this);
        v.findViewById(R.id.tv_2_hour).setOnClickListener(this);
        v.findViewById(R.id.tv_6_hour).setOnClickListener(this);
        v.findViewById(R.id.tv_24_hour).setOnClickListener(this);
        rateWindow.setContentView(v);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Boolean b = super.onTouchEvent(event);
        if (rateWindow != null)
            if (rateWindow.isShowing())
                rateWindow.dismiss();
        return b;
    }

    @Override
    public void onBackPressed() {
        if (rateWindow != null)
            if (rateWindow.isShowing())
                rateWindow.dismiss();
        super.onBackPressed();
    }

    @Override
    public void refreshViews(boolean openService, boolean loadImage, boolean openNotification ,int updateRate, int alpha) {
        mSwitchImage.setChecked(loadImage);
        mSwitchService.setChecked(openService);
        mNotification.setChecked(openNotification);
        mRate.setText(updateRate + "个小时");
        mRate.setTag(updateRate);
        mAlphaSeekBar.setProgress(alpha);
        mAlpha.setText("" + alpha);
        if(!mSwitchService.isChecked()) {
            mRlRate.setEnabled(false);
            mRateTitle.setTextColor(getResources().getColor(R.color.colorTextGray));
        } else {
            mRlRate.setEnabled(true);
            mRateTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_1_hour:
                mRate.setText("1个小时");
                mRate.setTag(1);
                break;
            case R.id.tv_2_hour:
                mRate.setText("2个小时");
                mRate.setTag(2);
                break;
            case R.id.tv_6_hour:
                mRate.setText("6个小时");
                mRate.setTag(6);
                break;
            case R.id.tv_24_hour:
                mRate.setText("24个小时");
                mRate.setTag(24);
                break;
        }
        rateWindow.dismiss();
    }
}
