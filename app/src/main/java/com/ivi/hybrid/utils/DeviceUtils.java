package com.ivi.hybrid.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.file.CloseUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/6/29.
 */

public class DeviceUtils {
    /**
     * 获取手机唯一mac地址，无需要设置权限以及不需要打开WIFI
     *
     * @return
     */
    public static String getMac() {
        String macSerial = "";
        String str = "";
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
            // 三星手机定制，获取地址为cat /sys/class/net/eth0/address
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            ir = new InputStreamReader(pp.getInputStream(),"UTF-8");
            input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            CloseUtils.closeIO(ir, input);
        }
        LogUtil.d("getMac: " + macSerial);
        return macSerial;
    }

    /**
     * 获取手机唯一设备号
     *
     * @return 返回mac地址和手机设备号等硬件信息的拼接
     */
    private static String getDevicesId() {
        StringBuilder sb = new StringBuilder();
        String mac = getMac();// 通过Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address")   无需要打开WiFi
        if (!TextUtils.isEmpty(mac)) {
            sb.append(mac).append(";");
        }
        sb.append("Build.FINGERPRINT=" + Build.FINGERPRINT).append(";");//硬件设备唯一识别号
        sb.append("Build.SERIAL=" + Build.SERIAL).append(";");// 2.3以及以上可以获取到值
        sb.append("Build.BOARD=" + Build.BOARD).append(";");
        sb.append("Build.MANUFACTURER=" + Build.MANUFACTURER).append(";");
        sb.append("Build.BRAND=" + Build.BRAND).append(";");
        sb.append("Build.DISPLAY=" + Build.DISPLAY).append(";");
        sb.append("Build.HARDWARE=" + Build.HARDWARE);
        return sb.toString();
    }


    public static String getDeviceId() {
        String devicesId = getDevicesId();
        return MD5.md5(devicesId);
    }

    /**
     * 获取设备信息
     */
    public static String getDeviceInfo() {
        Map<String, String> infos = new HashMap<>();
        try {
            PackageManager pm = Hybrid.get().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Hybrid.get().getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return new JSONObject(infos).toString();
    }
}
