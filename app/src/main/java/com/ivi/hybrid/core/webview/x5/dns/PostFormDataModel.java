package com.ivi.hybrid.core.webview.x5.dns;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author: Rea.X
 * date: 2017/9/22.
 */

public class PostFormDataModel implements Serializable {

    private static final long serialVersionUID = 5164673627310206511L;

    @SerializedName("url")
    private String url;
    @SerializedName("data")
    private List<DataModel> data;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
