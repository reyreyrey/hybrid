package com.ivi.hybrid.gesture.config;

import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.ColorInt;

/**
 * author: Rea.X
 * date: 2018/1/27.
 */

public class GestureConfig extends AbstractGestureConfig {

    private GestureConfig() {
    }

    public static class Builder implements IGestureConfig {
        private GestureConfig gestureConfig = new GestureConfig();


        @Override
        public Builder maxTryCount(int maxTryCount) {
            gestureConfig.setGestureMaxTryCount(maxTryCount);
            return this;
        }

        @Override
        public Builder viewConfig(AbstractViewConfig viewConfig) {
            gestureConfig.setViewConfig(viewConfig);
            return this;
        }

        @Override
        public Builder nodeSrcDrawable(Drawable nodeSrc) {
            gestureConfig.setNodeOnSrc(nodeSrc);
            return this;
        }

        @Override
        public Builder nodeOnSrcDrawable(Drawable nodeOnSrc) {
            gestureConfig.setNodeOnSrc(nodeOnSrc);
            return this;
        }

        @Override
        public Builder lineColor(@ColorInt int lineColor) {
            gestureConfig.setLineColor(lineColor);
            return this;
        }

        @Override
        public Builder pwdMinLength(int length) {
            gestureConfig.setPwdMinLength(length);
            return this;
        }

        @Override
        public Builder lineWidth(int lineWidth) {
            gestureConfig.setLineWidth(lineWidth);
            return this;
        }

        @Override
        public Builder enableVibrate(boolean enableVibrate) {
            gestureConfig.setEnableVibrate(enableVibrate);
            return this;
        }

        @Override
        public Builder nodeAnimation(@AnimRes int animation) {
            gestureConfig.setNodeAnimation(animation);
            return this;
        }


        @Override
        public Builder background(Drawable background) {
            gestureConfig.setBackground(background);
            return this;
        }

        @Override
        public GestureConfig build() {
            return gestureConfig;
        }
    }
}
