package com.tans.tweather.activity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tans.tweather.R;
import com.tans.tweather.adapter.CitiesRecyclerAdapter;
import com.tans.tweather.iviews.AddCityActivityView;
import com.tans.tweather.presenter.AddCityActivityPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by mine on 2018/3/20.
 */
@EActivity(R.layout.activity_add_city)
public class AddCityActivity extends AppCompatActivity implements AddCityActivityView {
    @ViewById(R.id.tool_bar)
    Toolbar mToolbar;

    @ViewById(R.id.rv_cities)
    RecyclerView mRvCites;

    @ViewById(R.id.tv_loading)
    TextView mLoading;

    @ViewById(R.id.rl_container)
    RelativeLayout mContainer;

    @ViewById(R.id.tv_parent_city)
    TextView mParentCity;

    AddCityActivityPresenter mPresenter;

    int mAnimaRadius = 25;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
    }

    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new AddCityActivityPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            boolean result = mPresenter.backToParent();
            if (!result) {
                destroy();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initRecyclerView(CitiesRecyclerAdapter adapter) {
        mRvCites.setAdapter(adapter);
        mRvCites.setLayoutManager(new LinearLayoutManager(this));
        mRvCites.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setLoadingViewEnable(boolean b) {
        if (b) {
            mLoading.setVisibility(View.VISIBLE);
        } else {
            mLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void startMoveReveal(Animator.AnimatorListener listener) {
        long width = mContainer.getWidth();
        long height = mContainer.getHeight();
        int radius = (int) Math.sqrt((double) width * width + height * height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(mContainer, (int) width - mAnimaRadius, (int) height - mAnimaRadius, mAnimaRadius, radius);
            animator.addListener(listener);
            animator.start();
        }
    }

    @Override
    public void startBackReveal(Animator.AnimatorListener listener) {
        long width = mContainer.getWidth();
        long height = mContainer.getHeight();
        int radius = (int) Math.sqrt((double) width * width + height * height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(mContainer, 0, 0, mAnimaRadius, radius);
            animator.addListener(listener);
            animator.start();
        }
    }

    public void destroy() {
        finish();
        overridePendingTransition(R.anim.anim_acitivity_enter,R.anim.anim_activity_exit);
    }

    @Override
    public void refreshParentCity(String city) {
        mParentCity.setVisibility(View.VISIBLE);
        mParentCity.setText(city);
    }

    @Override
    public void hideParentCity() {
        mParentCity.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

}
