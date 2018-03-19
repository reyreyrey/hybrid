package com.ivi.hybrid.core.update;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rea.X on 2017/2/15.
 */

public class VersionModel implements Serializable{
    private static final long serialVersionUID = -3880741734926829314L;
    @SerializedName("content")
    private String content;
    @SerializedName("src")
    private String src;
    @SerializedName("version_num")
    private String version_num;
    @SerializedName("version_num_required")
    private String version_num_required;
    @SerializedName("is_update")
    private boolean is_update;
    @SerializedName("is_remaind")
    private boolean is_remaind;
    @SerializedName("is_delta")
    private boolean is_delta;
    @SerializedName("delta_link")
    private String delta_link;
    @SerializedName("delta_version")
    private String delta_version;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getVersion_num() {
        return version_num;
    }

    public void setVersion_num(String version_num) {
        this.version_num = version_num;
    }

    public String getVersion_num_required() {
        return version_num_required;
    }

    public void setVersion_num_required(String version_num_required) {
        this.version_num_required = version_num_required;
    }

    public boolean isIs_update() {
        return is_update;
    }

    public void setIs_update(boolean is_update) {
        this.is_update = is_update;
    }

    public boolean isIs_remaind() {
        return is_remaind;
    }

    public void setIs_remaind(boolean is_remaind) {
        this.is_remaind = is_remaind;
    }

    public boolean isIs_delta() {
        return is_delta;
    }

    public void setIs_delta(boolean is_delta) {
        this.is_delta = is_delta;
    }

    public String getDelta_link() {
        return delta_link;
    }

    public void setDelta_link(String delta_link) {
        this.delta_link = delta_link;
    }

    public String getDelta_version() {
        return delta_version;
    }

    public void setDelta_version(String delta_version) {
        this.delta_version = delta_version;
    }

    @Override
    public String toString() {
        return "AutoUpdateModel{" +
                "content='" + content + '\'' +
                ", src='" + src + '\'' +
                ", version_num='" + version_num + '\'' +
                ", version_num_required='" + version_num_required + '\'' +
                ", is_update=" + is_update +
                ", is_remaind=" + is_remaind +
                ", is_delta=" + is_delta +
                ", delta_link='" + delta_link + '\'' +
                ", delta_version='" + delta_version + '\'' +
                '}';
    }
}
