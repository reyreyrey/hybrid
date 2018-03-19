package com.ivi.hybrid.core.net.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/23.
 */

public class InterceptModel implements Serializable {
    private static final long serialVersionUID = 1275834214781943190L;
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("code_http")
    public int codeHttp;
    @SerializedName("code_system")
    public String codeSystem;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCodeHttp() {
        return codeHttp;
    }

    public void setCodeHttp(int codeHttp) {
        this.codeHttp = codeHttp;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    @Override
    public String toString() {
        return "InterceptModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", codeHttp=" + codeHttp +
                ", codeSystem='" + codeSystem + '\'' +
                '}';
    }
}
