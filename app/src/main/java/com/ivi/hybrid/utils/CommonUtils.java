package com.ivi.hybrid.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.hotfix.Patcher;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.ivi.hybrid.core.Hybrid.LOCAL_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_TEST_MODEL;

/**
 * author: Rea.X
 * date: 2017/3/15.
 * <p>通用工具类</p>
 */

public class CommonUtils {
    /**
     * 检测view点击多少次
     *
     * @param view
     */
    public static void onClickNumber(final Context context, View view) {
        Observable ob = RxView.clicks(view).share();
        ob.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Notification>() {
                    @Override
                    public void accept(Notification o) throws Exception {
                    }
                })
                .buffer(ob.debounce(500, TimeUnit.MILLISECONDS))
                .map(new Function<List<Void>, Integer>() {
                    @Override
                    public Integer apply(List<Void> voids) throws Exception {
                        return voids.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer o) throws Exception {
                        if (o >= 5) {
                            showVersionDialog(context);
                        }
                    }
                });
    }


    private static void showVersionDialog(Context context) {
        String environment = "";
        switch (Hybrid.getRunModel()) {
            case RUNTIME_MODEL:
                environment = context.getString(R.string.hybrid_environment_runtime);
                break;
            case RUNTIME_TEST_MODEL:
                environment = context.getString(R.string.hybrid_environment_runtime_test);
                break;
            case LOCAL_MODEL:
                environment = context.getString(R.string.hybrid_environment_natival_test);
                break;
            default:
                break;
        }
        ToastUtils.toastSuccess(
                context,
                String.format(context.getString(R.string.hybrid_environment_version),
                        environment,
                        getVersionNameOriginal(context)
                                + "--H5:" + VersionTools.getDeltaVersion()
                                + "--hotfix:" + Patcher.getPatcherVersion()
                )
        );
    }

    public static String getVersionNameOriginal(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo e = pm.getPackageInfo(context.getPackageName(), 0);
            return e.versionName;
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
            return "1";
        }
    }

    public static Intent getInstallIntent(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = Hybrid.get().getResources().getString(R.string.AutoUpdateprovide);
            uri = FileProvider.getUriForFile(Hybrid.get(), auth, apkFile);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo e = pm.getPackageInfo(context.getPackageName(), 0);
            return (String) pm.getApplicationLabel(e.applicationInfo);
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
            return "1";
        }
    }

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        if (context == null) return false;
        boolean isOk = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] networks = manager.getAllNetworkInfo();
            for (NetworkInfo network : networks) {
                NetworkInfo.State state = network.getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    isOk = true;
                    break;
                }
            }
        }
        return isOk;
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            // 得到当前包的相关信息
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            if (!TextUtils.isEmpty(versionName) && versionName.contains("-")) {
                int index = versionName.indexOf("-");
                if (index > 0) {
                    versionName = versionName.substring(0, index);
                    return versionName;
                }
            }
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1";
        }
    }

    public static int getAppIcon(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断是否安装了微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 获取程序下载文件夹
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File f = context.getExternalFilesDir("");
            if (f == null) {
                path = context.getFilesDir().getAbsolutePath();
            } else {
                path = f.getAbsolutePath();
            }
        } else {
            path = context.getFilesDir().getAbsolutePath();
        }
        return path;
    }

    public static String getNameFromUrl(String url) {
        Uri uri = Uri.parse(url);
        List<String> paths = uri.getPathSegments();
        if (paths != null && paths.size() > 0) {
            String filename = paths.get(paths.size() - 1);
            if (!TextUtils.isEmpty(filename) && filename.contains(".")) {
                String[] names = filename.split("\\.");
                if (names != null && names.length > 1) {
                    String expend = names[names.length - 1];
                    return MD5.md5(url) + "." + expend;
                }
            }
        }
        return url;
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String fromatSize(long size) {

        long b = 1024L;
        long kb = 1024L * 1024L;
        long mb = 1024L * 1024L * 1024L;
        long gb = 1024L * 1024L * 1024L * 1024L;
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < b) {
            return size + "bytes";
        } else if (size < kb) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < mb) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < gb) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }

    }


    /**
     * 获取进程名字
     *
     * @param context
     * @param pID
     * @return
     */
    public static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        if (null == l) {
            return null;
        }
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    /**
     * 获取是否是主进程，Application中判断是否重复初始化
     *
     * @param context
     * @return false:不是服务进程    true:是服务进程
     */
    public static boolean isServicePid(Context context) {
        int pid = Process.myPid();
        String processName = getAppName(context, pid);
        if (processName != null && processName.equalsIgnoreCase(context.getPackageName())) {
            return false;
        }
        return true;
    }

    /**
     * sp转化为px
     *
     * @param context
     * @param value
     * @return
     */
    public static int sp2px(Context context, float value) {
        float f = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value * f + 0.5f);
    }


    /**
     * 获取程序是否在前台
     *
     * @return
     */
    public static boolean isAppInTheForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 复制到剪贴板
     *
     * @param context
     * @param s
     */
    public static void copyToClip(Context context, String s) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("", s);
        manager.setPrimaryClip(data);
    }


    public static boolean checkNotifyEnable(final Context context) {
        if (!NotificationUtil.hasOpenNotify(context)) {
            new AlertDialog.Builder(context).setMessage(String.format(context.getString(R.string.hybrid_notify_error_text), context.getPackageManager().getApplicationLabel(context.getApplicationInfo())))
                    .setNegativeButton(context.getString(R.string.hybrid_common_cancel), null).setPositiveButton(context.getString(R.string.hybrid_common_open), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startSetting(context);
                    } catch (Exception e) {
                    }
                }
            }).create().show();
            return false;
        }
        return true;
    }


    /**
     * 跳转到设置界面
     */
    public static void startSetting(Context context) {
        if (MiuiOs.isMIUI()) {
            Intent intent = MiuiOs.getSettingIntent(context);
            if (MiuiOs.isIntentAvailable(context, intent)) {
                context.startActivity(intent);
                return;
            }
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                context.startActivity(intent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }


    /**
     * 获取启动Activity的类
     *
     * @return
     */
    public static Class<Activity> getLaunchActivity() {
        try {
            PackageInfo packageInfo = Hybrid.get().getPackageManager().getPackageInfo(Hybrid.get().getPackageName(), 0);
            Intent resovleIntent = new Intent(Intent.ACTION_MAIN, null);
            resovleIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resovleIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> list = Hybrid.get().getPackageManager().queryIntentActivities(resovleIntent, 0);
            ResolveInfo resolveInfo = list.iterator().next();
            if (resolveInfo != null) {
                String className = resolveInfo.activityInfo.name;
                return (Class<Activity>) Class.forName(className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
