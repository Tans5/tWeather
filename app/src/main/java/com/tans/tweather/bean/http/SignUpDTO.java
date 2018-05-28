package com.tans.tweather.bean.http;

import com.tans.tweather.bean.account.UserBean;

public class SignUpDTO {
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
    }
}
