package com.tans.tweather.bean.account;

public class SettingBean {
    private int id;
    private int userId;
    private boolean loadImage;
    private boolean loadService;
    private int updateRate;
    private int imageAlpha;
    private boolean loadNotification;

    public boolean isLoadNotification() {
        return loadNotification;
    }

    public void setLoadNotification(boolean loadNotification) {
        this.loadNotification = loadNotification;
    }

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

    public boolean isLoadImage() {
        return loadImage;
    }

    public void setLoadImage(boolean loadImage) {
        this.loadImage = loadImage;
    }

    public boolean isLoadService() {
        return loadService;
    }

    public void setLoadService(boolean loadService) {
        this.loadService = loadService;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    public int getImageAlpha() {
        return imageAlpha;
    }

    public void setImageAlpha(int imageAlpha) {
        this.imageAlpha = imageAlpha;
    }
}
