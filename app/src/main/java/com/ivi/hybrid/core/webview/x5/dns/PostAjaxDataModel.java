package com.ivi.hybrid.core.webview.x5.dns;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/9/22.
 */

public class PostAjaxDataModel implements Serializable {
    private static final long serialVersionUID = -5447775043371945416L;

    @SerializedName("url")
    private UrlBean url;

    public UrlBean getUrl() {
        return url;
    }

    public void setUrl(UrlBean url) {
        this.url = url;
    }

    public static class UrlBean implements Serializable {
        private static final long serialVersionUID = 5774943545374634602L;

        @SerializedName("url")
        private String url;
        @SerializedName("data")
        private String data;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
