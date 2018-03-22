package com.tans.tweather.presenter;

import android.util.Log;

import com.tans.tweather.activity.AddCityActivity;
import com.tans.tweather.adapter.CitiesRecyclerAdapter;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.iviews.AddCityActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.manager.SpManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mine on 2018/3/20.
 */

public class AddCityActivityPresenter implements CitiesRecyclerAdapter.ItemClickListener {
    public static String TAG = AddCityActivityPresenter.class.getSimpleName();

    private AddCityActivityView mView;

    private ChinaCitiesManager mChinaCitiesManager;

    private List<LocationBean> data0;
    private List<LocationBean> data1;
    private List<LocationBean> data2;

    private CitiesRecyclerAdapter mAdapter;

    private int currentLevel = 1;

    private SpManager mSpManager = null;

    public AddCityActivityPresenter(AddCityActivityView view) {
        this.mView = view;
        mChinaCitiesManager = new ChinaCitiesManager();
        data0 = mChinaCitiesManager.queryCitiesByParentCode(ChinaCitiesManager.ROOT_CITY_PARENT_CODE);
        mAdapter = new CitiesRecyclerAdapter((AddCityActivity)mView,this);
        mAdapter.setData(data0);
        mView.initRecyclerView(mAdapter);
        mSpManager = SpManager.newInstance();
        mSpManager.initSp(BaseApplication.getInstance());
    }

    @Override
    public void onClick(int position, int level) {
        Log.d(TAG,"level: "+level+" position: "+position);
        switch (level) {
            case 1:
                data1 = mChinaCitiesManager.queryCitiesByParentCode(data0.get(position).getCode());
                mAdapter.setData(data1);
                currentLevel =2;
                break;
            case 2:
                data2 = mChinaCitiesManager.queryCitiesByParentCode(data1.get(position).getCode());
                mAdapter.setData(data2);
                currentLevel =3;
                break;
            case 3:
                saveCommonUserCity(data2.get(position).getCityName());
                break;
        }
    }

    private void saveCommonUserCity(String city) {
        List<String> cities = mSpManager.getCommonUseCities();
        if(cities.contains(city)) {
            return;
        } else {
            cities.add(city);
            mSpManager.storeCommonUseCities(cities);
        }
    }

    public boolean backToParent() {
        boolean b = false;
        switch (currentLevel) {
            case 1 :
                b = false;
                break;
            case  2:
                mAdapter.setData(data0);
                currentLevel--;
                b = true;
                break;
            case 3:
                mAdapter.setData(data1);
                currentLevel--;
                b = true;
                break;
        }
        return b;
    }

    public void onDestroy() {
        mView = null;
    }
}
