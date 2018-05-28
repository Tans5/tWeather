package com.tans.tweather.manager;

import com.tans.tweather.bean.account.CityBean;
import com.tans.tweather.bean.account.SettingBean;
import com.tans.tweather.bean.account.UserBean;
import com.tans.tweather.bean.http.LogInDTO;
import com.tans.tweather.bean.http.SignUpDTO;
import com.tans.tweather.bean.http.StoreDTO;
import com.tans.tweather.utils.ToastUtils;
import com.tans.tweather.utils.UrlUtils;
import com.tans.tweather.utils.httprequest.BaseHttpRequestUtils;

import java.util.ArrayList;
import java.util.List;

public class UserAccountManager {

    public static UserAccountManager instance;

    private BaseHttpRequestUtils mHttpRequestUtils;

    private ChinaCitiesManager mChinaCitiesManager;

    private SettingsManager mSettingsManager;

    private UserBean mUser;

    public interface LoginListener {
        void onSuccess();
        void onFail(String e);
    }

    public interface SignUpListener {
        void onSuccess();
        void onFail(String e);
    }

    public interface SettingUploadListener {
        void onSuccess();
        void onFail(String e);
    }

    public static UserAccountManager newInstance() {
        if(instance == null) {
            instance = new UserAccountManager();
        }
        return instance;
    }

    public void initDependencies(BaseHttpRequestUtils httpRequestUtils, ChinaCitiesManager chinaCitiesManager,
                                 SettingsManager settingsManager) {
        this.mHttpRequestUtils = httpRequestUtils;
        this.mChinaCitiesManager = chinaCitiesManager;
        this.mSettingsManager = settingsManager;
    }

    public void signUp(UserBean user, final SignUpListener listener) {
        SignUpDTO.Request request = new SignUpDTO.Request();
        request.setUser(user);
        mHttpRequestUtils.request(UrlUtils.getLoginBaseUrl(),
                UrlUtils.getSignUpPath(),
                BaseHttpRequestUtils.HttpRequestMethod.POST,
                request,
                new BaseHttpRequestUtils.HttpRequestListener<SignUpDTO.Response>() {
                    @Override
                    public void onSuccess(SignUpDTO.Response result) {
                        if(result.getResult()) {
                            mUser = result.getUser();
                            listener.onSuccess();
                        } else {
                            listener.onFail("注册失败，重新输入用户名和密码");
                        }
                    }

                    @Override
                    public Class<SignUpDTO.Response> getResultType() {
                        return SignUpDTO.Response.class;
                    }

                    @Override
                    public void onFail(String e) {
                        listener.onFail(e);
                    }
                });
    }

    public void logIn(UserBean user, final LoginListener listener) {
        LogInDTO.Request request = new LogInDTO.Request();
        request.setUser(user);
        mHttpRequestUtils.request(UrlUtils.getLoginBaseUrl(),
                UrlUtils.getSignInPath(),
                BaseHttpRequestUtils.HttpRequestMethod.POST,
                request,
                new BaseHttpRequestUtils.HttpRequestListener<LogInDTO.Response>() {
                    @Override
                    public void onSuccess(LogInDTO.Response result) {
                        if(result.getResult()) {
                            mUser = result.getUser();
                            saveSetting(result.getSetting());
                            saveCites(result.getCity());
                            listener.onSuccess();
                        } else {
                            listener.onFail("输入数据有错哦～");
                        }
                    }

                    @Override
                    public Class getResultType() {
                        return LogInDTO.Response.class;
                    }

                    @Override
                    public void onFail(String e) {
                        listener.onFail(e);
                    }
                });
    }

    public void settingUpload(final SettingUploadListener listener) {
        if(mUser != null) {
            StoreDTO.Request request = new StoreDTO.Request();
            SettingBean setting = new SettingBean();
            CityBean city = new CityBean();
            setting.setImageAlpha(mSettingsManager.getAlpha());
            setting.setUpdateRate(mSettingsManager.getRate());
            setting.setLoadImage(mSettingsManager.isLoadImage());
            setting.setLoadNotification(mSettingsManager.isOpenNotification());
            setting.setLoadService(mSettingsManager.isOpenService());
            setting.setUserId(mUser.getId());
            request.setUserId(mUser.getId());
            request.setSetting(setting);
            city.setUserId(mUser.getId());
            city.setCurrentCity(mChinaCitiesManager.getCurrentCity());
            city.setCommonCities(commonCities2String(mChinaCitiesManager.getCommonCities()));
            request.setCity(city);

            mHttpRequestUtils.request(UrlUtils.getLoginBaseUrl(),
                    UrlUtils.getStorePath(),
                    BaseHttpRequestUtils.HttpRequestMethod.POST,
                    request,
                    new BaseHttpRequestUtils.HttpRequestListener<StoreDTO.Response>() {
                        @Override
                        public void onSuccess(StoreDTO.Response result) {
                            if(result.getResult()) {
                                if(listener != null) {
                                    listener.onSuccess();
                                }
                                ToastUtils.getInstance().showShortText("同步数据到服务器成功！！");
                            } else {
                                if(listener != null) {
                                    listener.onFail("同步设置到服务器失败");
                                }
                            }
                        }

                        @Override
                        public Class getResultType() {
                            return StoreDTO.Response.class;
                        }

                        @Override
                        public void onFail(String e) {
                            if(listener != null) {
                                listener.onFail(e);
                            }
                        }
                    });
        } else {
        }
    }

    public void logOut() {
        mUser = null;
    }

    public UserBean getUser() {
        return mUser;
    }

    private UserAccountManager() {

    }

    private String commonCities2String(List<String> list) {
        String result = "";
        for(int i=0;i < list.size();i++) {
            if(i != list.size()-1) {
                result = result + list.get(i) + ",";
            } else {
                result = result + list.get(i);
            }
        }
        return  result;
    }

    private List<String> string2CommonCities (String s) {
        String [] ss = s.split(",");
        List<String> result = new ArrayList<>();

        for(int i =0 ; i<ss.length;i++) {
            if(!ss[i] .equals(""))
                result.add(ss[i]);
        }
        return result;
    }

    private void saveSetting(SettingBean setting) {
        if(setting != null) {
            mSettingsManager.setAlpha(setting.getImageAlpha());
            mSettingsManager.setLoadImage(setting.isLoadImage());
            mSettingsManager.setOpenNotification(setting.isLoadNotification());
            mSettingsManager.setRate(setting.getUpdateRate());
            mSettingsManager.setOpenService(setting.isLoadService());
            mSettingsManager.save();
        }
    }

    private void saveCites(CityBean city) {
        if(city != null) {
            mChinaCitiesManager.setCurrentCity(city.getCurrentCity());
            mChinaCitiesManager.setCommonCities(string2CommonCities(city.getCommonCities()));
        }
    }

}
