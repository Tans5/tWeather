package com.tans.tweather.bean;

import java.io.Serializable;

/**
 * Created by mine on 2017/11/21.
 */

public class AtmosphereBean implements Serializable {
    private int humidity;
    private float pressure;
    private int rising;
    private float visibility;

    public AtmosphereBean()
    {}

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getRising() {
        return rising;
    }

    public void setRising(int rising) {
        this.rising = rising;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }
}
