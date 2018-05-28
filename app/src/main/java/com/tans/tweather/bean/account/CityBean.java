package com.tans.tweather.bean.account;

public class CityBean {
    int id;
    int userId;
    String commonCities;
    String currentCity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommonCities() {
        return commonCities;
    }

    public void setCommonCities(String commonCities) {
        this.commonCities = commonCities;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }
}
