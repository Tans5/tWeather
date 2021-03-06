package com.tans.tweather.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tans on 2018/4/7.
 */

public class SettingsManager {
    private int alpha;
    private int rate;
    private boolean loadImage;
    private boolean openService;
    private boolean openNotification;
    private static SettingsManager instance;
    private SpManager spManager;
    private List<SettingsChangeListener> listeners;

    public static SettingsManager newInstance() {
        if(instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public interface SettingsChangeListener {
        void settingsChange();
    }

    public void initDependencies(SpManager spManager) {
        this.spManager = spManager;
    }

    public boolean isOpenService() {
        openService = spManager.getOpenService();
        return openService;
    }

    public void setOpenService(boolean openService) {
        this.openService = openService;
    }

    public boolean isLoadImage() {
        loadImage = spManager.getLoadBing();
        return loadImage;
    }

    public void setOpenNotification(boolean openNotification) {
        this.openNotification = openNotification;
    }

    public boolean isOpenNotification() {
        openNotification = spManager.getOpenNotification();
        return openNotification;
    }

    public void setLoadImage(boolean loadImage) {
        this.loadImage = loadImage;
    }

    public int getRate() {
        rate = spManager.getUpdateRate();
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getAlpha() {
        alpha = spManager.getWallPaperAlpha();
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void save() {
        spManager.storeLoadBing(loadImage);
        spManager.storeUpdateRate(rate);
        spManager.storeWallPaperAlpha(alpha);
        spManager.storeOpenService(openService);
        spManager.storeOpenNotification(openNotification);
        notifySettingsChange();
    }

    public void registerListener(SettingsChangeListener listener) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        if(listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(SettingsChangeListener listener) {
        if(listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    private SettingsManager() {

    }

    private void notifySettingsChange() {
        if(listeners != null) {
            for (SettingsChangeListener listener : listeners) {
                listener.settingsChange();
            }
        }
    }
}
