package com.ivi.hybrid.core.modules.forward;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/17.
 */

public class ForwardModel implements Serializable {
    private static final long serialVersionUID = -4465020292228623165L;

    @SerializedName("url")
    private String url;
    @SerializedName("browser")
    private boolean browser;
    @SerializedName("newView")
    private boolean newView;
    @SerializedName("gameType")
    private String gameType;

    @SerializedName("theme")
    private int theme;

    /**
     * 是否需要新开webview跳转
     */
    @SerializedName("force")
    private boolean force;

    @SerializedName("fullscreen")
    private boolean fullscreen;

    /**
     * 菜单类型
     */
    @SerializedName("menu")
    private int menu;

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBrowser() {
        return browser;
    }

    public void setBrowser(boolean browser) {
        this.browser = browser;
    }

    public boolean isNewView() {
        return newView;
    }

    public void setNewView(boolean newView) {
        this.newView = newView;
    }

    @Override
    public String toString() {
        return "JumpModel{" +
                "url='" + url + '\'' +
                ", browser=" + browser +
                ", newView=" + newView +
                ", gameType='" + gameType + '\'' +
                ", theme=" + theme +
                ", menu=" + menu +
                '}';
    }
}
