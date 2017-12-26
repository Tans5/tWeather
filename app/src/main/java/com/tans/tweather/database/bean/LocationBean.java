package com.tans.tweather.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mine on 2017/12/26.
 */
@DatabaseTable(tableName = "location")
public class LocationBean {
    @DatabaseField(id = true,columnName = "code")
    String code;

    @DatabaseField(canBeNull = true,columnName = "parent_code")
    String parentCode;

    @DatabaseField(canBeNull = false,columnName = "level")
    int level;

    @DatabaseField(canBeNull = false,columnName = "city_name")
    String cityName;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
