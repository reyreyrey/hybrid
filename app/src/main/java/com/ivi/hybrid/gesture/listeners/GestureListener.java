package com.ivi.hybrid.gesture.listeners;

import android.support.v4.app.DialogFragment;

/**
 * author: Rea.X
 * date: 2018/1/31.
 */

public interface GestureListener {

    void onGestureInputFinish(DialogFragment dialogFragment, String pwd, boolean isRight);
}
