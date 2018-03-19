package com.ivi.hybrid.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * author: Rea.X
 * date: 2017/11/14.
 */

public class MemoryUtils {

    public static String getMemoryString() {
        try {
            Context context = Hybrid.get();
            String memoryFormatString = context.getString(R.string.hybrid_memory_format);
            String avaiMemory = getAvailMemory(context);
            String totleMemory = getTotalMemory(context);
            String appMemory = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appMemory = getMemory();
            }
            String strTotleMemory = totleMemory == null ? context.getString(R.string.hybrid_memory_error) : String.valueOf(totleMemory);
            String stravaiMemory = avaiMemory == null ? context.getString(R.string.hybrid_memory_error) : String.valueOf(avaiMemory);
            String strappMemory = appMemory == null ? context.getString(R.string.hybrid_memory_error) : String.valueOf(appMemory);
            return String.format(memoryFormatString, strTotleMemory, strappMemory, stravaiMemory);
        } catch (Exception e) {
        }
        return "";
    }

    //获取可用运存大小
    public static String getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            if (am != null) {
                am.getMemoryInfo(mi);

                return  CommonUtils.fromatSize(mi.availMem);
            }
            return null;
        } catch (Exception e) {
            LogUtil.e("getAvailMemory error::" + e.toString());
            return null;
        }
    }

    //获取总运存大小
    public static String getTotalMemory(Context context) {
        try {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory = 0;
            FileReader localFileReader = new FileReader(str1);
            if(localFileReader == null)return null;
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            String s = arrayOfString[1];
            initial_memory = Long.valueOf(s).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
            return CommonUtils.fromatSize(initial_memory);
        } catch (Exception e) {
            LogUtil.e("getTotalMemory error::" + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getMemory() {
        try {
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            // dalvikPrivateClean + nativePrivateClean + otherPrivateClean;
            int totalPrivateClean = memoryInfo.getTotalPrivateClean();
            // dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty;
            int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();
            // dalvikPss + nativePss + otherPss;
            int totalPss = memoryInfo.getTotalPss();
            // dalvikSharedClean + nativeSharedClean + otherSharedClean;
            int totalSharedClean = memoryInfo.getTotalSharedClean();
            // dalvikSharedDirty + nativeSharedDirty + otherSharedDirty;
            int totalSharedDirty = memoryInfo.getTotalSharedDirty();
            // dalvikSwappablePss + nativeSwappablePss + otherSwappablePss;
            int totalSwappablePss = memoryInfo.getTotalSwappablePss();

            //return totalPrivateClean + totalPrivateDirty + totalPss + totalSharedClean + totalSharedDirty + totalSwappablePss;
            return CommonUtils.fromatSize(totalPrivateDirty * 1024);
        } catch (Exception e) {
            LogUtil.e("getMemory error::" + e.toString());
        }
        return null;
    }
}
