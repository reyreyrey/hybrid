package com.ivi.hybrid.core.call;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/7/27.
 */

public class CallJSModel implements Serializable{
    private static final long serialVersionUID = 8889598938791670526L;

    @SerializedName("requestId")
    private String requestId;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private String data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppInvokeJSModel{" +
                "requestId='" + requestId + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
