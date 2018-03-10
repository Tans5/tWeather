package com.tans.tweather.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.dao.Dao;
import com.tans.tweather.application.BaseApplication;
import com.tans.tweather.database.DatabaseHelper;
import com.tans.tweather.database.bean.LocationBean;
import com.tans.tweather.interfaces.ILatestWeatherInfoManager;
import com.tans.tweather.interfaces.INetRequestUtils;
import com.tans.tweather.utils.NetRequestUtils;

import java.sql.SQLException;
import java.util.List;

import static com.tans.tweather.utils.NetRequestUtils.LOCATION_URL;
import static com.tans.tweather.utils.NetRequestUtils.LOCATION_URL_TAIL;

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
    private Context mContext = null;

    /**
     * 城市信息更新的监听
     */
    public interface LoadCurrentCityListener {
        void onSuccess(String s);

        void onFail(VolleyError e);
    }

    public ChinaCitiesManager () {
        mDatabaseHelper = DatabaseHelper.getHelper(BaseApplication.getInstance());
        mNetRequestUtils = NetRequestUtils.newInstance();
        mContext = BaseApplication.getInstance();
        mNetRequestUtils.setContext(mContext);
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

    /**
     * 获取当前城市
     * @param listener
     */
    public void loadCurrentCity(final LoadCurrentCityListener listener) {

        if (!mNetRequestUtils.isNetWorkAvailable()) {
            VolleyError volleyError = new VolleyError("网络不可用");
            listener.onFail(volleyError);
            return;
        }

        mNetRequestUtils.requestLocationInfo(new INetRequestUtils.NetRequestListener() {
            @Override
            public void onSuccess(Object result) {

                listener.onSuccess(result.toString());
//                mCurrentCity = result.toString();
//                mSpManager.storeCurrentUseCity(result.toString());//写入到sp中
            }

            @Override
            public void onFail(VolleyError e) {
                listener.onFail(e);
            }
        },getLocation());
    }

    /**
     * 获取当前位置，需要用户开启 权限
     *
     * @return 返回精度和纬度的double数组，空的话就是没有权限。
     */
    private double[] getLocation() {
        if (mContext == null)
            return null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String provider = "";
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains((LocationManager.GPS_PROVIDER))) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            return null;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double[] result = {latitude, longitude};
        return result;
    }

    private String[] splitCityString(String s) {
        return s.split(",");
    }

    private String[] splitCityNameAndCode(String s) {
        return s.split("\\|");
    }
}