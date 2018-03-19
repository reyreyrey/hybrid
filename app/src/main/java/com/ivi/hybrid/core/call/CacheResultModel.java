package com.ivi.hybrid.core.call;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/30.
 */

public class CacheResultModel implements Serializable {


    private static final long serialVersionUID = 3400568873440200466L;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private String data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CacheResultModel{" +
                "requestId='" + requestId + '\'' +
                ", status=" + status +
                ", data='" + data + '\'' +
                '}';
    }
}
