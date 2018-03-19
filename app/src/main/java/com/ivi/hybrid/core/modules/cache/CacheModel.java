package com.ivi.hybrid.core.modules.cache;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/17.
 */

public class CacheModel implements Serializable{
    private static final long serialVersionUID = 7217257017353375374L;

    @SerializedName("key")
    private String key;
    @SerializedName("value")
    private String value;
    @SerializedName("expire")
    private long expire;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "CacheModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", expire=" + expire +
                '}';
    }
}
