package com.tans.tweather.utils;


import android.widget.Toast;

import com.tans.tweather.application.BaseApplication;

/**
 * Created by mine on 2017/12/28.
 */

public class ToastUtils {


    private static ToastUtils instance = null;
    private Toast toast = null;

    private ToastUtils() {
        toast = Toast.makeText(BaseApplication.getInstance(),"",Toast.LENGTH_SHORT);
        instance = this;
    }

    public static ToastUtils getInstance() {
        if(instance == null)
            return new ToastUtils();
        else
            return instance;
    }

    public void showShortText(String s) {
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }

    public void showLongText(String s) {
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(s);
        toast.show();
    }
}
