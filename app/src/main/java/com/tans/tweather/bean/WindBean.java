package com.tans.tweather.bean;

import java.io.Serializable;

/**
 * Created by mine on 2017/11/21.
 */

public class WindBean implements Serializable {
    private int chill;
    private int direction;
    private int speed;


    public WindBean()
    {

    }

    public int getChill() {
        return chill;
    }

    public void setChill(int chill) {
        this.chill = chill;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
