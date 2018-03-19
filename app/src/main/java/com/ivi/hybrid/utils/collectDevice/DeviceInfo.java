package com.ivi.hybrid.utils.collectDevice;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.MD5;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;


/**
 * author: Rea.X
 * date: 2017/5/5.
 */

public class DeviceInfo {

    private static final String TAG = DeviceInfo.class.getSimpleName()
            + "_channel";


    /**
     * 获取手机唯一mac地址，无需要设置权限以及不需要打开WIFI
     *
     * @return
     */
    public static String getMac() {
        String macSerial = "";
        String str = "";
        try {
            // 三星手机定制，获取地址为cat /sys/class/net/eth0/address
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
     * @return 手机型号
     */
    public static String getPhoneModel() {

        return Build.MODEL.replaceAll(" ", "-");
    }

    public static String getPhoneBoard() {
        return Build.BOARD.replaceAll(" ", "-") + "-" + Build.MANUFACTURER.replaceAll(" ", "-");
    }

    /**
     * 获取系统语言
     *
     * @return
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * @return SDK版本号
     */
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * @return 手机系统版本号
     */
    public static String getFirmwareVersion() {
        return Build.VERSION.RELEASE.replaceAll(" ", "-");
    }
}
