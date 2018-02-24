package com.tans.tweather.interfaces;

import java.util.Set;

/**
 * Created by tans on 2017/11/19.
 */

public interface ISpManager {


    /**
     * 储存常用城市
     * @param commonUseCities
     */
    void storeCommonUseCities(String commonUseCities);

    /**
     * 返回常用城市
     * @return
     */
    Set<String> getCommonUseCities();

    /**
     * 储存当前使用城市
     * @param city
     */
    void storeCurrentUseCity(String city);

    /**
     * 获取当前使用城市
     * @return
     */
    String getCurrentUseCity();

    /**
     * 储存桌面大图透明度
     * @param a
     */
    void storeWallPaperAlpha(int a);

    /**
     * 获取透明度
     * @return
     */
    int getWallPaperAlpha();
}
