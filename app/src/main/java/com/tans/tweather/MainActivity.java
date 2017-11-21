package com.tans.tweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tans.tweather.bean.ForecastBean;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetRequestUtils netRequestUtils = NetRequestUtils.newInstance();
        netRequestUtils.requestForecast(new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                ArrayList<ForecastBean> result1 = (ArrayList<ForecastBean>)result;
                Toast.makeText(getApplication(),result1.get(0).getItem().getForecast().getText(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(IOException e) {

            }
        });
    }
}
