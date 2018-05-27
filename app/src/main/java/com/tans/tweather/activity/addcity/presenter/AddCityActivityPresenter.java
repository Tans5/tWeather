package com.tans.tweather.activity.addcity.presenter;

import android.util.Log;

import com.tans.tweather.activity.addcity.AddCityActivity;
import com.tans.tweather.activity.addcity.adapter.CitiesRecyclerAdapter;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.dagger2.component.DaggerAddCityActivityComponent;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.mvp.Presenter;
import com.tans.tweather.mvp.view.AddCityActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.dagger2.module.PresenterModule;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mine on 2018/3/20.
 */

public class AddCityActivityPresenter implements Presenter, CitiesRecyclerAdapter.ItemClickListener {
    public static String TAG = AddCityActivityPresenter.class.getSimpleName();

    private AddCityActivityView mView;

    private List<LocationBean> data0;
    private List<LocationBean> data1;
    private List<LocationBean> data2;

    private String parent0;

    private CitiesRecyclerAdapter mAdapter;

    private int currentLevel = 1;

    @Inject
    ChinaCitiesManager mChinaCitiesManager;

    public AddCityActivityPresenter(AddCityActivityView view) {
        this.mView = view;
    }

    @Override
    public void initDependencies() {
        DaggerAddCityActivityComponent.builder()
                .applicationComponent(BaseApplication.getApplicationComponent())
                .presenterModule(new PresenterModule(mView))
                .build()
                .inject(this);
        mAdapter = new CitiesRecyclerAdapter((AddCityActivity) mView, this);
        mView.initRecyclerView(mAdapter);
    }

    public void initRootCity() {
        mView.setLoadingViewEnable(true);
        mChinaCitiesManager.queryChildrenCities(ChinaCitiesManager.ROOT_CITY_PARENT_CODE, ChinaCitiesManager.ROOT_CITY_LEVEL,
                new ChinaCitiesManager.LoadChildrenCityListener() {
                    @Override
                    public void onSuccess(List<LocationBean> childCities) {
                        data0 = childCities;
                        mAdapter.setData(data0);
                        mView.setLoadingViewEnable(false);
                    }

                    @Override
                    public void onFail(String e) {
                        mView.showToast(e.toString());
                        mView.setLoadingViewEnable(false);
                    }
                });
    }

    @Override
    public void onClick(int position, int level) {
        Log.d(TAG, "level: " + level + " position: " + position);
        switch (level) {
            case 1:
                mView.setLoadingViewEnable(true);
                mView.refreshParentCity(data0.get(position).getCityName());
                parent0 = data0.get(position).getCityName();
                updateData(data0.get(position).getCode(), 2);
                break;
            case 2:
                mView.setLoadingViewEnable(true);
                mView.refreshParentCity(data1.get(position).getCityName());
                updateData(data1.get(position).getCode(), 3);
                break;
            case 3:
                saveCommonUserCity(data2.get(position).getCityName());
                break;
        }
    }

    public boolean backToParent() {
        boolean b = false;
        switch (currentLevel) {
            case 1:
                b = false;
                break;
            case 2:
                mAdapter.setData(data0);
                mView.hideParentCity();
                currentLevel--;
                b = true;
                break;
            case 3:
                mAdapter.setData(data1);
                mView.refreshParentCity(parent0);
                currentLevel--;
                b = true;
                break;
        }
        return b;
    }

    @Override
    public void onDestroy() {
        mView = null;
        data0 = null;
        data1 = null;
        data2 = null;
        mAdapter = null;
        mChinaCitiesManager = null;
    }

    private void updateData(final String parentCode, final int level) {
        mChinaCitiesManager.queryChildrenCities(parentCode, level, new ChinaCitiesManager.LoadChildrenCityListener() {
            @Override
            public void onSuccess(List<LocationBean> childCities) {
                int level = childCities.get(0).getLevel();
                currentLevel = level;
                if (level == 2) {
                    data1 = childCities;
                    mAdapter.setData(data1);
                } else if (level == 3) {
                    data2 = childCities;
                    mAdapter.setData(data2);
                }
                mView.setLoadingViewEnable(false);
            }

            @Override
            public void onFail(String e) {
                mView.showToast(e);
            }
        });
    }

    private void saveCommonUserCity(String city) {
        List<String> cities = mChinaCitiesManager.getCommonCities();
        if (cities.contains(city)) {
            mView.showToast("该城市已经添加");
            return;
        } else {
            cities.add(city);
            mChinaCitiesManager.setCommonCities(cities);
            mView.showToast("添加成功");
            mView.destroyActivity();
        }
    }
}
