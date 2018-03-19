package com.ivi.hybrid.gesture.utils.cache;

import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.gesture.config.AbstractViewConfig;
import com.ivi.hybrid.utils.MD5;

/**
 * author: Rea.X
 * date: 2018/1/30.
 */

public class GestureCache extends AbstractGestureCache {
    private static final String GESTURE_REMAIN_COUNT = "gesture_remain_count";
    private static final String GESTURE_PWD = "gesture_pwd";
    private static final String IS_OPEN_GESTURE = "is_open_gesture";
    private static final String IS_APP_IN_FORWARD = "is_app_in_forward";

    private GestureCache() {
    }

    private static GestureCache gestureCache;

    public static GestureCache getInstant() {
        if (gestureCache == null)
            gestureCache = new GestureCache();
        return gestureCache;
    }

    @Override
    public void saveRemainCount(int count) {
        saveInteger(GESTURE_REMAIN_COUNT, count);
    }

    @Override
    public void gestureInputWrong() {
        int remainCount = getRemainCount();
        remainCount = remainCount - 1;
        saveRemainCount(remainCount);
    }

    @Override
    public void reset() {
        saveRemainCount(Hybrid.getGestureConfig().getGestureMaxTryCount());
    }

    @Override
    public int getRemainCount() {
        return getInteger(GESTURE_REMAIN_COUNT, Hybrid.getGestureConfig().getGestureMaxTryCount());
    }

    @Override
    public void saveGesturePwd(String pwd) {
        pwd = MD5.md5(pwd);
        saveString(GESTURE_PWD, pwd);
    }

    @Override
    public boolean checkGesturePwd(String pwd) {
        pwd = MD5.md5(pwd);
        String localPwd = getString(GESTURE_PWD);
        return pwd.equalsIgnoreCase(localPwd);
    }

    @Override
    public boolean isOpenGesture() {
        String isOpen = getString(IS_OPEN_GESTURE);
        if (!TextUtils.isEmpty(isOpen)) {
            try {
                return Boolean.parseBoolean(isOpen);
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    @Override
    public void saveOpenGesture(boolean isOpenGesture) {
        saveString(IS_OPEN_GESTURE, String.valueOf(isOpenGesture));
    }

    @Override
    public boolean isSetPassword() {
        String localPwd = getString(GESTURE_PWD);
        return !TextUtils.isEmpty(localPwd);
    }

    @Override
    public void appGotoBack() {
        saveString(IS_APP_IN_FORWARD, String.valueOf(true));
    }

    @Override
    public void gestureDismiss() {
        saveString(IS_APP_IN_FORWARD, String.valueOf(false));
    }

    @Override
    public boolean isAppFormBackToForward() {
        String value = getString(IS_APP_IN_FORWARD);
        if(TextUtils.isEmpty(value))return false;
        try{
            return Boolean.valueOf(value);
        } catch (Exception ignored){}
        return false;
    }
}
