package com.tans.tweather.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;

import com.tans.tweather.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tans on 2018/4/7.
 */

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    @ViewById(R.id.tool_bar)
    Toolbar mToolbar;

    @AfterViews
    void init() {
        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
