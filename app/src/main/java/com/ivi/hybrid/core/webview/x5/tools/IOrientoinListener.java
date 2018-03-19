package com.ivi.hybrid.core.webview.x5.tools;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.util.Log;
import android.view.OrientationEventListener;

import static com.tencent.smtt.sdk.TbsReaderView.TAG;

/**
 * author: Rea.X
 * date: 2017/4/4.
 */

public class IOrientoinListener extends OrientationEventListener {
    private Activity context;

    public IOrientoinListener(Activity context) {
        super(context);
        this.context = context;
    }

    public IOrientoinListener(Activity context, int rate) {
        super(context, rate);
        this.context = context;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
            Looper.loop();
        }
        Log.d(TAG, "orention" + orientation);
        int screenOrientation = context.getResources().getConfiguration().orientation;
        if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                Log.d(TAG, "设置竖屏");
                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else if (orientation > 225 && orientation < 315) { //设置横屏
            Log.d(TAG, "设置横屏");
            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (orientation > 45 && orientation < 135) {// 设置反向横屏
            Log.d(TAG, "反向横屏");
            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        } else if (orientation > 135 && orientation < 225) {
            Log.d(TAG, "反向竖屏");
            if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
        }
    }
}
