package com.tans.tweather.bean.http;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 鹏程 on 2018/5/25.
 */

public class HttpGetParams {
    Map<String,String> p;
    public HttpGetParams() {
        p = new HashMap<>();
    }
    public HttpGetParams add(String key,String value) {
        p.put(key,value);
        return this;
    }

    public String toHttpGetParamsString() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for(String key : p.keySet()) {
            i = i + 1;
            if( i == 1) {
                result.append("?");
            } else {
                result.append("&");
            }
            result.append(key)
                    .append("=")
                    .append(URLEncoder.encode(p.get(key)));
        }
        return result.toString();
    }

    public Map<String, String> getParams() {
        return p;
    }
}
