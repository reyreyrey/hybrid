package com.ivi.hybrid.core.modules.driver;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/7/29.
 */

public class CopyClipeModel implements Serializable {
    private static final long serialVersionUID = -6750003013681167166L;
    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CopyClipeModel{" +
                "value='" + value + '\'' +
                '}';
    }
}
