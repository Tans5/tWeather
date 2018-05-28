package com.tans.tweather.manager;

import com.tans.tweather.bean.account.UserBean;
import com.tans.tweather.bean.request.LogInDTO;
import com.tans.tweather.bean.request.SignUpDTO;
import com.tans.tweather.utils.UrlUtils;
import com.tans.tweather.utils.httprequest.BaseHttpRequestUtils;

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

    public void logOut() {
        mUser = null;
    }

    public UserBean getUser() {
        return mUser;
    }

    private UserAccountManager() {

    }

}
