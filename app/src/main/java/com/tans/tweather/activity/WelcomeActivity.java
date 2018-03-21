package com.tans.tweather.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.tans.tweather.R;

/**
 * Created by mine on 2018/3/20.
 */

public class WelcomeActivity extends AppCompatActivity {

    String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        transStatusColor();
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(260);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                changeActivity();
            }
        }.execute();
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
        Log.d(TAG,"onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG,"onRestart");
        super.onRestart();
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                finish();
                overridePendingTransition(R.anim.anim_acitivity_enter,R.anim.anim_activity_exit);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        if(Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(findViewById(R.id.tv_app_name), "toolbar")).toBundle());
        //    finish();
        } else {
            startActivity(intent);
            overridePendingTransition(R.anim.anim_acitivity_enter,R.anim.anim_activity_exit);
        }
    }
}
