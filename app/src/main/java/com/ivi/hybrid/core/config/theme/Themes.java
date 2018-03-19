package com.ivi.hybrid.core.config.theme;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/8/4.
 */

public class Themes implements Serializable {
    private static final long serialVersionUID = 5509429832503248622L;

    //状态栏背景
    @SerializedName("statusBarbackground")
    private Drawable statusBarbackground;
    //返回按钮
    @SerializedName("backIcon")
    private
    @DrawableRes
    int backIcon;
    //toolbar背景
    @SerializedName("toolbarBackground")
    private Drawable toolbarBackground;
    @SerializedName("titleTextColor")
    private
    @ColorInt
    int titleTextColor;

    @SerializedName("isDefault")
    private boolean isDefault;

    //底部虚拟栏颜色值
    @SerializedName("navigationBarColor")
    private
    @ColorInt
    int navigationBarColor;


    public
    @ColorInt
    int getNavigationBarColor() {
        return navigationBarColor;
    }

    public void setNavigationBarColor(@ColorInt int navigationBarColor) {
        this.navigationBarColor = navigationBarColor;
    }

    public Drawable getStatusBarbackground() {
        return statusBarbackground;
    }

    public void setStatusBarbackground(Drawable statusBarbackground) {
        this.statusBarbackground = statusBarbackground;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }


    public
    @DrawableRes
    int getBackIcon() {
        return backIcon;
    }

    public void setBackIcon(@DrawableRes int backIcon) {
        this.backIcon = backIcon;
    }

    public Drawable getToolbarBackground() {
        return toolbarBackground;
    }

    public void setToolbarBackground(Drawable toolbarBackground) {
        this.toolbarBackground = toolbarBackground;
    }

    public
    @ColorInt
    int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }


}
