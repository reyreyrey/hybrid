package com.ivi.hybrid.utils.notify;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

/**
 * author: Rea.X
 * date: 2017/4/28.
 */

public class NatificationUtil {

    /**
     * 是否开启了通知
     * @param context
     * @return true:开启
     */
    public static boolean hasOpenNotify(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
