package com.ivi.hybrid.utils;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

/**
 * author: Rea.X
 * date: 2017/4/28.
 */

public class NotificationUtil {

    /**
     * 是否开启了通知
     * @param context
     * @return true:开启
     */
    public static boolean hasOpenNotify(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
