package com.ivi.hybrid.core.push.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class Cons {

    public static final String SERVICE_ACTION = "com.rea.push.service";

    public static final String ACTION_ALAMER = "com.rea.push.alarm";

    public static final String ACTION_CLICK = "com.rea.push.CLICK_NOTIFY";

    public static final String ACTION_MESSAGE = "com.rea.push.RECEIVER_MESSAGE";

    public static final String MESSAGE_DIALOG = "com.rea.push.RECEIVER_MESSAGE_DIALOG";

    public static final long HEART_BEAT_TIME = 30 * 1000;

}
