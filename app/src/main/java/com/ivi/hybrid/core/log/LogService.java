package com.ivi.hybrid.core.log;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ivi.hybrid.core.daemon.Daemon;
import com.ivi.hybrid.utils.LogUtil;

/**
 * author: Rea.X
 * date: 2017/11/14.
 */

public class LogService extends Service implements Runnable {
    //第一次启动时延迟60秒然后检测上传
    private static final long FIRST_TIME = 60 * 1000;
    //之后每隔30分钟检测一次
    private static final long UPLOAD_TIME = 30 * 60 * 1000;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.d("LogService oncreate");

        Daemon.compatibleO2(this);

        //app创建的时候，
        checkUpload();
    }

    private void checkUpload() {
        handler.postDelayed(this, FIRST_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("LogService onStartCommand");
        checkUpload();
        return START_STICKY;
    }

    @Override
    public void run() {
        handler.removeCallbacks(this);
        Log.upload(this);
        handler.postDelayed(this, UPLOAD_TIME);
    }
}
