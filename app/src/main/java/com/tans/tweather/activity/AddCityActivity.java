package com.tans.tweather.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;

import com.tans.tweather.R;
import com.tans.tweather.iviews.AddCityActivityView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
    }

    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
