package com.ivi.hybrid.core.net.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rea.X on 2017/2/2.
 */

public class TokenModel implements Serializable{
    private static final long serialVersionUID = -597152177337291752L;
    @SerializedName("appToken")
    private String appToken;

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "appToken='" + appToken + '\'' +
                '}';
    }
}
