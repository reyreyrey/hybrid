package com.ivi.hybrid.gesture;

import android.support.v4.app.FragmentActivity;

import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.gesture.config.GestureConfig;
import com.ivi.hybrid.gesture.listeners.GestureListener;
import com.ivi.hybrid.gesture.ui.GestureLockFragment;
import com.ivi.hybrid.gesture.ui.GestureSettingFragment;
import com.ivi.hybrid.gesture.ui.GestureValidationFragment;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;

/**
 * author: Rea.X
 * date: 2018/1/29.
 */

public class GestureManager {

    public static void showGestureLock(FragmentActivity activity) {
        showGestureLock(activity, null);
    }


    public static void showGestureSetting(FragmentActivity activity) {
        showGestureSetting(activity, null);
    }


    public static void showGestureValidation(FragmentActivity activity) {
        showGestureValidation(activity, null);
    }

    public static void showGestureLock(FragmentActivity activity, GestureListener listener) {
        if (!isOpenGesture()) return;
        if (!GestureCache.getInstant().isSetPassword()) {
            showGestureSetting(activity, listener);
            return;
        }
        GestureLockFragment fragment = GestureLockFragment.getInstant();
        if (fragment == null) return;
        fragment.show(activity, listener);
    }


    public static void showGestureSetting(FragmentActivity activity, GestureListener listener) {
        if (!isOpenGesture()) return;
        GestureSettingFragment fragment = GestureSettingFragment.getInstant();
        if (fragment == null) return;
        fragment.show(activity, listener);
    }


    public static void showGestureValidation(FragmentActivity activity, GestureListener listener) {
        if (!isOpenGesture()) return;
        GestureValidationFragment fragment = GestureValidationFragment.getInstant();
        if (fragment == null) return;
        fragment.show(activity, listener);
    }

    public static boolean isOpenGesture() {
        boolean a = Config.isOpenGesture();
        boolean b = GestureCache.getInstant().isOpenGesture();
        boolean c = UserHelper.INSTANT.isLogin();
        return a && b && c;
    }
}
