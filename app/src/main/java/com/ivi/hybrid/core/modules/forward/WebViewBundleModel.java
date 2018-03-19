package com.ivi.hybrid.core.modules.forward;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class WebViewBundleModel implements Serializable {
    private static final long serialVersionUID = -2094477939792742226L;

    //跳转的url
    public String url;
    //是否是打开游戏
    public boolean isGame;
    //从预置主题中选择哪一种
    public int theme;
    //是否需要全屏
    public boolean fullScreen;
    //从预置的menu中选择哪一种
    public int menu;
    //是否可以退出页面
    public boolean enable_exit;
    //cookies
    public ArrayList<String> cookies;
    //是否强制打开新页面
    public boolean isForce;

    public WebViewBundleModel(String url, boolean isGame, int theme, boolean fullScreen, int menu) {
        this.url = url;
        this.isGame = isGame;
        this.theme = theme;
        this.fullScreen = fullScreen;
        this.menu = menu;
        this.enable_exit = true;
    }

    public WebViewBundleModel(String url, boolean isGame, int theme, boolean fullScreen, int menu, boolean enable_exit) {
        this.url = url;
        this.isGame = isGame;
        this.theme = theme;
        this.fullScreen = fullScreen;
        this.menu = menu;
        this.enable_exit = enable_exit;
    }

    public WebViewBundleModel(String url, boolean isGame, int theme, boolean fullScreen, int menu, ArrayList<String> cookies) {
        this(url, isGame, theme, fullScreen, menu, cookies, false);
    }

    public WebViewBundleModel(String url, boolean isGame, int theme, boolean fullScreen, int menu, ArrayList<String> cookies, boolean isForce) {
        this.url = url;
        this.isGame = isGame;
        this.theme = theme;
        this.fullScreen = fullScreen;
        this.menu = menu;
        this.enable_exit = true;
        this.cookies = cookies;
        this.isForce = isForce;
    }
}
