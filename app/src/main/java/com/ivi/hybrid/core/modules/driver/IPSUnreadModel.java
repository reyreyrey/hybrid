package com.ivi.hybrid.core.modules.driver;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/6/14.
 */

public class IPSUnreadModel implements Serializable {
    private static final long serialVersionUID = -2883198072063981266L;
    @SerializedName("num")
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "IPSUnreadModel{" +
                "num=" + num +
                '}';
    }
}
