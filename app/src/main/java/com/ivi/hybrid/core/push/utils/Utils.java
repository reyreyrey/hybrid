package com.ivi.hybrid.core.push.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;


import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.log.LogService;
import com.ivi.hybrid.core.push.services.PushService;
import com.ivi.hybrid.utils.LogUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import static com.ivi.hybrid.core.push.utils.Cons.ACTION_ALAMER;

/**
 * author: Rea.X
 * date: 2017/3/9.
 */

public class Utils {

    /**
     * 获取是否是小端序
     *
     * @return
     */
    private static boolean isLittleEndian() {
        return ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * 转化为小端序
     *
     * @param bytes
     * @return
     */
    public static byte[] orderLittleEndian(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.array();
    }

    /**
     * 二进制字符串转byte
     */
    public static byte binaryStr2Byte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    /**
     * 获取一个byte字节中的每一个bit
     *
     * @param b
     * @return
     */
    public static byte[] byte2byteArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    //byte 数组转int
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    //int 转byte数组
    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static byte[] intToByteArray2(int a) {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (result.length - 1 - i) * 8;
            result[3 - i] = (byte) ((a >>> offset) & 0xff);
        }
        return result;
    }

    /**
     * 将int转化为4字节的小端序字节数组
     *
     * @param a
     * @return
     */
    public static byte[] getLittleEndianByteArray(int a) {
        byte[] bs = intToByteArray(a);
        bs = orderLittleEndian(bs);
        return bs;
    }

    public static byte[] getLittleEndianByteArra2(int a) {
        byte[] bs = intToByteArray2(a);
        bs = orderLittleEndian(bs);
        return bs;
    }

    public static String logByteArray(byte[] bs) {
        return logByteArray(null, bs);
    }

    public static String logByteArray(String s, byte[] bs) {
        if (bs == null || bs.length == 0) {
            LogUtil.e("byte数组为空--------------");
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(s)) {
            sb.append(s);
            sb.append("-->");
        }
        sb.append("array>[");
        for (byte b : bs) {
            sb.append((b & 0xff));
            sb.append("  ");
        }
        sb.append("]");
        LogUtil.d(sb.toString());
        return sb.toString();
    }

    public static boolean pushServiceIsWork(Context context) {
        return serviceIsWork(context, PushService.class);
    }

    public static boolean logServiceIsWork(Context context) {
        return serviceIsWork(context, LogService.class);
    }

    public static boolean serviceIsWork(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServiceInfos) {
            if (info.service.getClassName().toString().equals(cls.getName()) && info.service.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context) {
        if (pushServiceIsWork(context)) return;
        if (!Config.isOpenPush()) return;
        startAlame(context);
        LogUtil.d("intent start service------------");
        Intent intent1 = new Intent(context, PushService.class);
        intent1.setAction(Cons.SERVICE_ACTION);
        intent1.setPackage(context.getPackageName());
        try {
            ContextCompat.startForegroundService(Hybrid.get(), intent1);
        } catch (Throwable e) {
            LogUtil.e("intent start service fail------------" + e.toString());
        }
    }

    public static void startAlame(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(ACTION_ALAMER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long time = SystemClock.elapsedRealtime() + 5 * 1000;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, 10 * 1000, pendingIntent);
    }
}
