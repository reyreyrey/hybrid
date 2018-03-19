package com.ivi.hybrid.utils.collectDevice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;

import java.util.ArrayList;
import java.util.List;

/**
 * AppInfo工具
 */
public class AppInfoUtils {
    private static final String TAG="AppInfoUtils";
    private static ArrayList<DeviceModel.AppInfo> sAppList;
    public static ArrayList<DeviceModel.AppInfo> getAppsData() {
        Context context = Hybrid.get();
        PackageManager pManager = context.getPackageManager();
        // 应用的信息
        ArrayList<DeviceModel.AppInfo> appList = new ArrayList<DeviceModel.AppInfo>();
        // 获取安卓包信息
        List<PackageInfo> pakList = pManager.getInstalledPackages(0);
        int index=0;
        for (int i = 0; i < pakList.size(); i++) {
            PackageInfo pak = pakList.get(i);
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
            {
                DeviceModel.AppInfo appItem = new DeviceModel.AppInfo();
                appItem.setAppName(pManager.getApplicationLabel(
                        pak.applicationInfo).toString());// 应用的名字
                appItem.setPackageName(pak.packageName); // 应用的包名
                if (context.getPackageName().equals(pak.packageName)) {
                    continue;
                }
                String versionName = "";
                try {
                    if (!TextUtils.isEmpty(pak.versionName)) {
                        versionName = pak.versionName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                appItem.setVersionName(versionName); // 应用的版本名字
                appItem.setVersionCode("" + pak.versionCode); // 应用的版本的Code
//                appItem.setIcon(pManager
//                        .getApplicationIcon(pak.applicationInfo));
                index=index+1;
                appItem.setTag(index); // 显示的数顺序，第一显示的顺序就是1
                appList.add(appItem);
            }
        }
        sAppList = appList;
        return appList;
    }

    public static int getAppCount() {
        return sAppList == null ? 0 : sAppList.size();
    }
}
