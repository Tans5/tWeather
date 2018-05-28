package com.tans.tweather.bean.request;

import com.tans.tweather.bean.account.CityBean;
import com.tans.tweather.bean.account.SettingBean;

public class StoreDTO {
    public static class Request {
        int userId;
        SettingBean setting;
        CityBean city;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
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

    public static class Response {
        boolean result;

        public boolean getResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }
}
