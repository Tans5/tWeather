package com.tans.tweather.manager;

import com.android.volley.VolleyError;
import com.j256.ormlite.dao.Dao;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.database.DatabaseHelper;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mine on 2017/12/28.
 */

public class ChinaCitiesManager {

    public static String TAG = ChinaCitiesManager.class.getSimpleName();
    public static int ROOT_CITY_LEVEL = 1;
    public static String ROOT_CITY_PARENT_CODE = "";
    public static int END_LEVEL = 3;

    private DatabaseHelper mDatabaseHelper = null;
    private Dao<LocationBean,String> mDao = null;
    private NetRequestUtils mNetRequestUtils = null;

    public ChinaCitiesManager () {
        mDatabaseHelper = DatabaseHelper.getHelper(BaseApplication.getInstance());
        mNetRequestUtils = NetRequestUtils.newInstance();
        mNetRequestUtils.setContext(BaseApplication.getInstance());
        try {
            mDao = mDatabaseHelper.getDao(LocationBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Init();
    }

    private void Init()  {
        LocationBean testCity = null;
        try {
            testCity = mDao.queryForId("01");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(testCity == null)
            InitCitiesInfo(ROOT_CITY_PARENT_CODE,ROOT_CITY_LEVEL);
    }

    private void InitCitiesInfo(final String parentCityCode, final int level) {

        mNetRequestUtils.requestCitiesInfo(new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {
                String s = result.toString();
                String[] cities = splitCityString(s);
                for(int i=0;i< cities.length;i++) {
                    String[] city = splitCityNameAndCode(cities[i]);
                    saveCity(city,parentCityCode,level);
                    if(level < END_LEVEL) {
                        InitCitiesInfo(city[0],level+1);
                    }
                }
            }

            @Override
            public void onFail(VolleyError e) {

            }
        },parentCityCode);

    }

    private void saveCity (String[] city,String parentCode,int level) {
        LocationBean locationBean = new LocationBean();
        locationBean.setParentCode(parentCode);
        locationBean.setLevel(level);
        locationBean.setCode(city[0]);
        locationBean.setCityName(city[1]);
        try {
            mDao.createIfNotExists(locationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LocationBean> queryCitiesByParentCode(String parentCode) {
        List<LocationBean> resultCities = null;
        try {
            resultCities = mDao.queryBuilder().where().eq("parent_code",parentCode).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultCities;
    }

    private String[] splitCityString(String s) {
        return s.split(",");
    }

    private String[] splitCityNameAndCode(String s) {
        return s.split("\\|");
    }
}
