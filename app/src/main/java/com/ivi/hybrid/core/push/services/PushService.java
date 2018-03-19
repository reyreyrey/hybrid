package com.ivi.hybrid.core.push.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ivi.hybrid.core.daemon.Daemon;
import com.ivi.hybrid.core.push.heartbeat.HeartbeatHelper;
import com.ivi.hybrid.core.push.utils.Utils;
import com.ivi.hybrid.utils.LogUtil;


/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class PushService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.compatibleO2(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("push service onStartCommand");
        try {
            HeartbeatHelper.startHeartbeat(this.getApplicationContext());
        } catch (Throwable e) {
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Utils.startService(this);
        } catch (Throwable e) {
        }
    }
}
