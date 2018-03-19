package com.ivi.hybrid.core.push.heartbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.core.push.callback.PushCallBack;
import com.ivi.hybrid.core.push.helper.PushHelper;
import com.ivi.hybrid.utils.LogUtil;

import java.util.UUID;


/**
 * author: Rea.X
 * date: 2017/3/9.
 */

public class HeartbeatHelper {
    private static PushHelper push;
    private static boolean isInit = false;
    private static HeartbeatRequestBody mScheduleRequestBody;


    public static void startHeartbeat(Context context) {
        LogUtil.d("isInit:"+isInit);
        if (isInit) return;
        isInit = true;
        push = PushHelper.getInstance();
        mScheduleRequestBody = new HeartbeatRequestBody(context);
        push.newScheduleRequest(mScheduleRequestBody, new PushCallBack(context, mScheduleRequestBody, push));
    }


    public static void shouldRefreshData() {
        if (push != null && mScheduleRequestBody != null)
            push.refreshData(mScheduleRequestBody);
    }


    private static String getUUID(byte[] bs) {
        return UUID.nameUUIDFromBytes(bs).toString();
    }
}
