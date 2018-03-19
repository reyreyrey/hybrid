package com.ivi.hybrid.core.bridge;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/5/17.
 */

public class AppBridgeModel implements Serializable {
    private static final long serialVersionUID = -735456841002152101L;

    @SerializedName("requestId")
    private String requestId;
    @SerializedName("service")
    private String service;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private Map<String,Object> data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppBridgeModel{" +
                "requestId='" + requestId + '\'' +
                ", service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
