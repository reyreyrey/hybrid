package com.ivi.hybrid.gesture.utils.cache;

import android.text.TextUtils;

import com.ivi.hybrid.core.call.CallModule;

/**
 * author: Rea.X
 * date: 2018/1/30.
 */

abstract class AbstractGestureCache implements IGestureCache {

    @Override
    public void saveString(String key, String value) {
        CallModule.invokeCacheModuleSave(key, value);
    }

    @Override
    public String getString(String key) {
        return CallModule.invokeCacheModuleGet(key);
    }

    @Override
    public void saveInteger(String key, Integer value) {
        CallModule.invokeCacheModuleSave(key, String.valueOf(value));
    }

    @Override
    public Integer getInteger(String key) {
        return getInteger(key, 0);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        String value = CallModule.invokeCacheModuleGet(key);
        if (!TextUtils.isEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }


    //设置剩余重试次数
    public abstract void saveRemainCount(int count);
    //设置剩余重试次数
    public abstract void gestureInputWrong();
    public abstract void reset();
    //获取剩余重试次数
    public abstract int getRemainCount();
    //设置手势密码
    public abstract void saveGesturePwd(String pwd);
    //获取手势密码是否正确
    public abstract boolean checkGesturePwd(String pwd);
    //获取是否开启手势密码
    public abstract boolean isOpenGesture();
    //设置是否开启手势密码
    public abstract void saveOpenGesture(boolean isOpenGesture);
    //是否设置了手势密码
    public abstract boolean isSetPassword();
    //app进入后台时
    public abstract void appGotoBack();
    //手势验证结束
    public abstract void gestureDismiss();
    //是否是从后台转前台
    public abstract boolean isAppFormBackToForward();
}
