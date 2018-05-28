package com.tans.tweather.bean.request;

import com.tans.tweather.bean.account.CityBean;
import com.tans.tweather.bean.account.SettingBean;
import com.tans.tweather.bean.account.UserBean;

public class LogInDTO {
    public static class Request {
        UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }
    }

    public static class Response {
        boolean result;
        UserBean user;
        SettingBean setting;
        CityBean city;

        public boolean getResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public SettingBean getSetting() {
            return setting;
        }

        public void setSetting(SettingBean setting) {
            this.setting = setting;
        }

        public CityBean getCity() {
            return city;
        }

        public void setCity(CityBean city) {
            this.city = city;
        }
    }
}
