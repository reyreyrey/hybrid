package com.ivi.hybrid.core.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.ivi.hybrid.core.daemon.Daemon;
import com.ivi.hybrid.core.net.callback.DownloadCallback;
import com.ivi.hybrid.core.net.download.DownLoad;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.file.FileUtils;

import java.io.File;

import static com.ivi.hybrid.utils.CommonUtils.getInstallIntent;
import static com.ivi.hybrid.utils.CommonUtils.getNameFromUrl;

/**
 * Created by Fish.C on 2017/11/9.
 */

public class DownloadAppService extends Service implements DownloadCallback {

    public static void downloadAppAndInstall(Context context, String apkUrl, boolean showNotify) {
        Intent intent = new Intent(context, DownloadAppService.class);
        intent.putExtra("url", apkUrl);
        intent.putExtra("showNotify", showNotify);
        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.compatibleO2(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getExtras() == null) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        String url = intent.getExtras().getString("url");
        boolean showNotify = intent.getBooleanExtra("showNotify", false);
        File apkFile = new File(CommonUtils.getCacheDir(this), getNameFromUrl(url));
        if (apkFile.exists()) {
            boolean f = install(apkFile);
            if (!f) {
                startDownload(url, showNotify);
                return START_STICKY;
            }
            stopSelf();
            return START_STICKY;
        }
        startDownload(url, showNotify);
        return START_STICKY;
    }

    private void startDownload(String completeUrl, boolean showNotify) {
        DownLoad.INSTANT.download(completeUrl, this, showNotify);
    }

    @Override
    public void requestSuccess(File file) {
        install(file);
        stopSelf();
    }

    @Override
    public void requestFial() {
        Intent intent = new Intent("downloadError");
        intent.setPackage(getPackageName());
        getApplicationContext().sendBroadcast(intent);
        stopSelf();
    }

    @Override
    public void downProgress(long readLength, long contentLength, int progress) {
        Intent intent = new Intent();
        intent.setAction("downloadProgress");
        intent.putExtra("currentSize", readLength);
        intent.putExtra("totalSize", contentLength);
        intent.putExtra("progress", progress);
        intent.setPackage(getPackageName());
        getApplicationContext().sendBroadcast(intent);
    }

    /**
     * 调用系统Intent安装apk包
     *
     * @param apkFile
     */
    private boolean install(File apkFile) {
        if (!checkApkFile(apkFile.getAbsolutePath())) return false;
        Intent intent1 = new Intent("dismiss");
        getApplicationContext().sendBroadcast(intent1);
        Intent intent = getInstallIntent(apkFile);
        this.getApplicationContext().startActivity(intent);
        return true;
    }


    /**
     * 检查apk文件是否有效(是正确下载,没有损坏的)
     *
     * @param apkFilePath
     * @return
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                delFile(apkFilePath);
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            delFile(apkFilePath);
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private void delFile(String path) {
        File f = new File(path);
        FileUtils.deleteFile(f);
    }

}
