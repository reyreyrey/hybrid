package com.ivi.hybrid.gesture.config;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.ResourceUtil;
import com.ivi.hybrid.utils.ScreenUtil;

/**
 * author: Rea.X
 * date: 2018/1/27.
 */

abstract class AbstractGestureConfig {

    //手势最多重试次数
    private int gestureMaxTryCount;
    //界面上下方自定义的view
    private AbstractViewConfig viewConfig;
    //正常状态下的手势圆圈
    private Drawable nodeSrc;
    //选中状态下的手势圆圈
    private Drawable nodeOnSrc;
    //连线的颜色值
    private
    @ColorInt
    int lineColor;
    //连线的宽度
    private int lineWidth;
    //是否允许震动
    private boolean enableVibrate;
    //界面背景色
    private Drawable background;
    //圆圈的动画
    private
    @AnimRes
    int nodeAnimation;
    //密码最少的长度
    private int pwdMinLength;

    AbstractGestureConfig() {
        pwdMinLength = 4;
        gestureMaxTryCount = 5;
        lineColor = Color.WHITE;
        viewConfig = new DefaultViewConfig();
        lineWidth = (int) Hybrid.getContext().getResources().getDimension(R.dimen.linehight);
        enableVibrate = true;
        nodeSrc = Hybrid.getContext().getResources().getDrawable(R.mipmap.gesture_bg_choice_join_n);
        nodeOnSrc = Hybrid.getContext().getResources().getDrawable(R.mipmap.gesture_bg_choice_join_s);
        background = Hybrid.getContext().getResources().getDrawable(R.drawable.gesture_background);
        nodeAnimation = R.anim.gesture_node_on_1;
    }

    public int getPwdMinLength() {
        return pwdMinLength;
    }

    public void setPwdMinLength(int pwdMinLength) {
        this.pwdMinLength = pwdMinLength;
    }

    public int getNodeAnimation() {
        return nodeAnimation;
    }

    public void setNodeAnimation(int nodeAnimation) {
        this.nodeAnimation = nodeAnimation;
    }

    public int getGestureMaxTryCount() {
        return gestureMaxTryCount;
    }

    public AbstractViewConfig getViewConfig() {
        return viewConfig;
    }

    public Drawable getNodeSrc() {
        return nodeSrc;
    }

    public Drawable getNodeOnSrc() {
        return nodeOnSrc;
    }

    public int getLineColor() {
        return lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public boolean isEnableVibrate() {
        return enableVibrate;
    }

    public Drawable getBackground() {
        return background;
    }

    public void setGestureMaxTryCount(int gestureMaxTryCount) {
        this.gestureMaxTryCount = gestureMaxTryCount;
    }

    public void setViewConfig(AbstractViewConfig viewConfig) {
        this.viewConfig = viewConfig;
    }

    public void setNodeSrc(Drawable nodeSrc) {
        this.nodeSrc = nodeSrc;
    }

    public void setNodeOnSrc(Drawable nodeOnSrc) {
        this.nodeOnSrc = nodeOnSrc;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setEnableVibrate(boolean enableVibrate) {
        this.enableVibrate = enableVibrate;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }
}
