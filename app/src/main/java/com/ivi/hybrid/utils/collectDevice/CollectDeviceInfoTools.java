package com.ivi.hybrid.utils.collectDevice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getDeviceId;

/**
 * author: Rea.X
 * date: 2017/6/30.
 */

public class CollectDeviceInfoTools {
    public static void collect() {
        if (!shouldCollectDeviceInfo()) return;
        HybridRequest.request(getCollectParams(), "app/collectDeviceInfo", new HybridRequestCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onSuccess(response);
                String result = response.body();
                if (!TextUtils.isEmpty(result)) {
                    InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                    if (interceptModel != null && interceptModel.status) {
                        CollectDeviceInfoTools.collectDeviceInfoSuccess();
                    }
                }
            }
        });
    }

    public static Map<String, String> getCollectParams() {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", getDeviceId());
        params.put("method", getMethod());
        params.put("info", getDeviceInfo());
        return params;
    }

    private static String getDeviceInfo() {
        String imei = getDeviceId();
        DeviceModel device = new DeviceModel();
        device.platform = DeviceInfo.getPhoneModel();
        device.board = DeviceInfo.getPhoneBoard();
        device.systemNameAndVersion = "Android "
                + DeviceInfo.getFirmwareVersion();
        device.macAddress = DeviceInfo.getMac();
        device.IMEINumber = imei;
        device.UUIDString = imei;
        device.carrier = getProvidersName();
        //需要权限，删除
//        device.carrier = DeviceInfo.getProvidersName();
        device.allAppList = AppInfoUtils.getAppsData();
        device.appVersion = CommonUtils.getVersionName(Hybrid.get());
        device.appKey = getAppKey();
        return JsonParse.toJson(device);
    }

    /**
     * @return 手机运营商
     */
    public static String getProvidersName() {
        String ProvidersName = "";
        try {
            TelephonyManager sTelephonyManager = (TelephonyManager) Hybrid.get().getSystemService(
                    Context.TELEPHONY_SERVICE);
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String IMSI = sTelephonyManager.getSubscriberId();
            if (TextUtils.isEmpty(IMSI)) {
                return "无运行商";
            }
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }

        } catch (Throwable e) {
            LogUtil.e("---------------------------------------------error start---------------------------------\t\n"+e.toString()+"---------------------------------------------error end---------------------------------\t\n");
        }
        return ProvidersName;
    }

    private static String getAppKey() {
        Context context = Hybrid.get();
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString("app_key");
        } catch (Throwable e) {
            LogUtil.e("---------------------------------------------error start---------------------------------\t\n"+e.toString()+"---------------------------------------------error end---------------------------------\t\n");
            e.printStackTrace();
            // 不可能发生.
            return "1";
        }
    }

    private static final String KEY = "version";

    private static String getMethod() {

        try {
            String version = CallModule.invokeCacheModuleGet(KEY);
            if (TextUtils.isEmpty(version)) return "install";
            int currentVersion = getCurrentVersion();
            if (version.equals(String.valueOf(currentVersion))) {
                return "update";
            }
        } catch (Exception e) {
            LogUtil.e("---------------------------------------------error start---------------------------------\t\n"+e.toString()+"---------------------------------------------error end---------------------------------\t\n");
            e.printStackTrace();
        }
        return "install";
    }

    private static int getCurrentVersion() throws PackageManager.NameNotFoundException {
        PackageManager pm = Hybrid.get().getPackageManager();
        PackageInfo info = null;
        info = pm.getPackageInfo(Hybrid.get().getPackageName(), 0);
        int currentVersion = info.versionCode;
        return currentVersion;
    }


    private static boolean shouldCollectDeviceInfo() {
        try {
            String version = CallModule.invokeCacheModuleGet(KEY);
            if (TextUtils.isEmpty(version)) return true;
            int currentVersion = getCurrentVersion();
            return !version.equals(String.valueOf(currentVersion));
        } catch (Exception e) {
            LogUtil.e("---------------------------------------------error start---------------------------------\t\n"+e.toString()+"---------------------------------------------error end---------------------------------\t\n");
            e.printStackTrace();
        }
        return true;
    }

    public static void collectDeviceInfoSuccess() {
        try {
            int currentVersion = getCurrentVersion();
            CallModule.invokeCacheModuleSave(KEY, String.valueOf(currentVersion));
        } catch (Exception e) {
            LogUtil.e("---------------------------------------------error start---------------------------------\t\n"+e.toString()+"---------------------------------------------error end---------------------------------\t\n");
        }
    }
}
