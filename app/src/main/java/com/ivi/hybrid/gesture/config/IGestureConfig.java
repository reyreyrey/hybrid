package com.ivi.hybrid.gesture.config;

import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.ColorInt;

/**
 * author: Rea.X
 * date: 2018/1/29.
 */

interface IGestureConfig {
    GestureConfig.Builder maxTryCount(int maxTryCount);

    GestureConfig.Builder viewConfig(AbstractViewConfig viewConfig);

    GestureConfig.Builder nodeSrcDrawable(Drawable nodeSrc);

    GestureConfig.Builder nodeOnSrcDrawable(Drawable nodeOnSrc);

    GestureConfig.Builder lineColor(@ColorInt int lineColor);

    GestureConfig.Builder pwdMinLength(int length);

    GestureConfig.Builder lineWidth(int lineWidth);

    GestureConfig.Builder enableVibrate(boolean enableVibrate);

    GestureConfig.Builder nodeAnimation(@AnimRes int animation);

    GestureConfig.Builder background(Drawable background);

    GestureConfig build();
}
