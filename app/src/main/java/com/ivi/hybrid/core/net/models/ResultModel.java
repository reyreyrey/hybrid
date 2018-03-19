package com.ivi.hybrid.core.net.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/11/1.
 */

public class ResultModel<T> implements Serializable {
    private static final long serialVersionUID = -8524820686144797563L;
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public T data;

    @Override
    public String toString() {
        return "ResultModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
