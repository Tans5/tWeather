package com.tans.tweather.utils;

import com.tans.tweather.R;

/**
 * 用于获取到的网络信息到界面展示信息的转换。
 * Created by tans on 2017/12/3.
 */

public class ResultTransUtils {

    /**
     *
     * @param code 天气的情况编号
     * @return 对应天气图标的drawable值
     */
    public static int getWeatherIconId(int code) {
        int id = R.drawable.weather_ic_undefine;
        switch (code) {
            case 0: {
                id = R.drawable.weather_ic_0;
                break;
            }
            case 1: {
                id = R.drawable.weather_ic_1;
                break;
            }
            case 2: {
                id = R.drawable.weather_ic_2;
                break;
            }
            case 3: {
                id = R.drawable.weather_ic_3;
                break;
            }
            case 4: {
                id = R.drawable.weather_ic_4;
                break;
            }
            case 5: {
                id = R.drawable.weather_ic_5;
                break;
            }
            case 6: {
                id = R.drawable.weather_ic_6;
                break;
            }
            case 7: {
                id = R.drawable.weather_ic_7;
                break;
            }
            case 8: {
                id = R.drawable.weather_ic_8;
                break;
            }
            case 9: {
                id = R.drawable.weather_ic_9;
                break;
            }
            case 10: {
                id = R.drawable.weather_ic_10;
                break;
            }
            case 11: {
                id = R.drawable.weather_ic_11;
                break;
            }
            case 12: {
                id = R.drawable.weather_ic_12;
                break;
            }
            case 13: {
                id = R.drawable.weather_ic_13;
                break;
            }
            case 14: {
                id = R.drawable.weather_ic_14;
                break;
            }
            case 15: {
                id = R.drawable.weather_ic_15;
                break;
            }
            case 16: {
                id = R.drawable.weather_ic_16;
                break;
            }
            case 17: {
                id = R.drawable.weather_ic_17;
                break;
            }
            case 18: {
                id = R.drawable.weather_ic_18;
                break;
            }
            case 19: {
                id = R.drawable.weather_ic_19;
                break;
            }
            case 20: {
                id = R.drawable.weather_ic_20;
                break;
            }
            case 21: {
                id = R.drawable.weather_ic_21;
                break;
            }
            case 22: {
                id = R.drawable.weather_ic_22;
                break;
            }
            case 23: {
                id = R.drawable.weather_ic_23;
                break;
            }
            case 24: {
                id = R.drawable.weather_ic_24;
                break;
            }
            case 25: {
                id = R.drawable.weather_ic_25;
                break;
            }
            case 26: {
                id = R.drawable.weather_ic_26;
                break;
            }
            case 27: {
                id = R.drawable.weather_ic_27;
                break;
            }
            case 28: {
                id = R.drawable.weather_ic_28;
                break;
            }
            case 29: {
                id = R.drawable.weather_ic_29;
                break;
            }
            case 30: {
                id = R.drawable.weather_ic_30;
                break;
            }
            case 31: {
                id = R.drawable.weather_ic_31;
                break;
            }
            case 32: {
                id = R.drawable.weather_ic_32;
                break;
            }
            case 33: {
                id = R.drawable.weather_ic_33;
                break;
            }
            case 34: {
                id = R.drawable.weather_ic_34;
                break;
            }
            case 35: {
                id = R.drawable.weather_ic_35;
                break;
            }
            case 36: {
                id = R.drawable.weather_ic_36;
                break;
            }
            case 37: {
                id = R.drawable.weather_ic_37;
                break;
            }
            case 38: {
                id = R.drawable.weather_ic_38;
                break;
            }
            case 39: {
                id = R.drawable.weather_ic_39;
                break;
            }
            case 40: {
                id = R.drawable.weather_ic_40;
                break;
            }
            case 41: {
                id = R.drawable.weather_ic_41;
                break;
            }
            case 42: {
                id = R.drawable.weather_ic_42;
                break;
            }
            case 43: {
                id = R.drawable.weather_ic_43;
                break;
            }
            case 44: {
                id = R.drawable.weather_ic_44;
                break;
            }
            case 45: {
                id = R.drawable.weather_ic_45;
                break;
            }
            case 46: {
                id = R.drawable.weather_ic_46;
                break;
            }
            case 47: {
                id = R.drawable.weather_ic_47;
                break;
            }

        }

        return id;
    }
}
