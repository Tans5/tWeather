package com.tans.tweather.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.tans.tweather.activity.AddCityActivity;
import com.tans.tweather.adapter.CitiesRecyclerAdapter;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.iviews.AddCityActivityView;
import com.tans.tweather.manager.ChinaCitiesManager;
import com.tans.tweather.utils.ToastUtils;

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

    private String parent0;

    private CitiesRecyclerAdapter mAdapter;

    private int currentLevel = 1;


    public AddCityActivityPresenter(AddCityActivityView view) {
        this.mView = view;
        mChinaCitiesManager = ChinaCitiesManager.newInstance();
        data0 = mChinaCitiesManager.queryCitiesByParentCode(ChinaCitiesManager.ROOT_CITY_PARENT_CODE);
        mAdapter = new CitiesRecyclerAdapter((AddCityActivity)mView,this);
        mAdapter.setData(data0);
        mView.initRecyclerView(mAdapter);
    }

    @Override
    public void onClick(int position, int level) {
        Log.d(TAG,"level: "+level+" position: "+position);
        switch (level) {
            case 1:
                mView.setLoadingViewEnable(true);
                mView.refreshParentCity(data0.get(position).getCityName());
                parent0 = data0.get(position).getCityName();
                updateData(data0.get(position).getCode());
                break;
            case 2:
                mView.setLoadingViewEnable(true);
                mView.refreshParentCity(data1.get(position).getCityName());
                updateData(data1.get(position).getCode());
                break;
            case 3:
                saveCommonUserCity(data2.get(position).getCityName());
                break;
        }
    }

    private void updateData(final String parentCode) {
        new AsyncTask<Void,Void,List<LocationBean>>() {

            @Override
            protected List<LocationBean> doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return mChinaCitiesManager.queryCitiesByParentCode(parentCode);
            }

            @Override
            protected void onPostExecute(List<LocationBean> locationBeen) {
                super.onPostExecute(locationBeen);
                int level = locationBeen.get(0).getLevel();
                currentLevel = level;
                if(level == 2) {
                    data1 = locationBeen;
                    mAdapter.setData(data1);
                } else if (level == 3) {
                    data2 = locationBeen;
                    mAdapter.setData(data2);
                }
                mView.setLoadingViewEnable(false);
            }
        }.execute();
    }

    private void saveCommonUserCity(String city) {
        List<String> cities = mChinaCitiesManager.getCommonCities();
        if(cities.contains(city)) {
            ToastUtils.getInstance().showShortText("该城市已经添加");
            return;
        } else {
            cities.add(city);
            mChinaCitiesManager.setCommonCities(cities);
            ToastUtils.getInstance().showShortText("添加成功");
            mView.destroy();
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

    public void onDestroy() {
        mView = null;
        data0 = null;
        data1 = null;
        data2 = null;
        mAdapter = null;
        mChinaCitiesManager = null;
    }
}
