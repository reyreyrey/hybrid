package com.ivi.hybrid.core.modules.ui;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/17.
 */

public class LoadingModel implements Serializable{
    private static final long serialVersionUID = 6217179038486839537L;
    @SerializedName("state")
    private String type;
    @SerializedName("delay")
    private int delay;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "LoadingModel{" +
                "type='" + type + '\'' +
                ", delay=" + delay +
                '}';
    }
}
