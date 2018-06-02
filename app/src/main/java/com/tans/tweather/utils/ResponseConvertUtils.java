package com.tans.tweather.utils;

import com.tans.tweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于获取到的网络信息到界面展示信息的转换。
 * Created by tans on 2017/12/3.
 */

public class ResponseConvertUtils {


    /**
     * @param result 网络请求返回的json 字符串
     * @return 描述大气的json字符串
     */
    public static String getAtmosphereJsonString(String result) {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("atmosphere");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param result
     * @return 描述风的json字符串
     */
    public static String getWindJsonString(String result) {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("wind");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param result
     * @return 描述当前天气的json字符串
     */
    public static String getCurrentConditionJsonString(String result) {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            jsonObject = (JSONObject) jsonObject.get("channel");
            jsonObject = (JSONObject) jsonObject.get("item");
            jsonObject = (JSONObject) jsonObject.get("condition");
            resultString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param result
     * @return 未来10天天气的字符串
     */
    public static String getFutureConditionJsonString(String result) {
        String resultString = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("query");
            jsonObject = (JSONObject) jsonObject.get("results");
            JSONArray jsonArray = (JSONArray) jsonObject.get("channel");
            resultString = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param result
     * @return 返回当前位置的城市
     */
    public static String getCurrentCityString(String result) {
        String city = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.get("result");
            jsonObject = (JSONObject) jsonObject.get("addressComponent");
            city = jsonObject.get("city").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return city;
    }

    public static String getBingImagePath(String imageGetWayResponse) {
        int start = imageGetWayResponse.indexOf("<url>") + 5;
        int end = imageGetWayResponse.indexOf("</url>");
        String result = imageGetWayResponse.substring(start,end);
        return result;
    }

    /**
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

    public static String getWindDirection(int angle) {
        String result = "";
        if (angle == 0) {
            result = "北";
        } else if (angle == 90) {
            result = "东";
        } else if (angle == 180) {
            result = "南";
        } else if (angle == 270) {
            result = "西";
        } else if (angle == 360) {
            result = "北";
        } else if (angle > 0 && angle < 90) {
            result = "东北";
        } else if (angle > 90 && angle < 180) {
            result = "东南";
        } else if (angle > 180 && angle < 270) {
            result = "西南";
        } else if (angle < 270 && angle < 360) {
            result = "西北";
        }
        return result;
    }
}
