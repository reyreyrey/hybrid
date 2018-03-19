package com.ivi.hybrid.core.modules.cache;

import android.text.TextUtils;


/**
 * author: Rea.X
 * date: 2017/3/9.
 */

public class PushPreferenceHelper extends PreferencesHelper {

    /**
     * 将uuid保存在本地
     *
     * @param uuid
     */
    public static void saveUUID(String uuid) {
        saveString("uuid", uuid);
    }

    /**
     * 获取本地存储的uuid
     *
     * @return
     */
    public static String getUUID() {
        return getString("uuid");
    }

    /**
     * 是否是第一次发心跳包
     *
     * @return true:第一次发送
     */
    public static boolean isFristHeartbeat() {
        return TextUtils.isEmpty(getUUID());
    }

    public static void saveUUIDBytes(byte[] bs) {
        for (int i = 0; i < bs.length; i++) {
            saveInt(i + "", bs[i]);
        }
    }

    public static void clearUUID() {
        for (int i = 0; i < 16; i++) {
            saveInt(i + "", 0);
        }
    }

    /**
     * 从SharedPreferences中读取uuid
     *
     * @return
     */
    public static byte[] getUUIDBytes() {
        byte[] bs = new byte[16];
        for (int i = 0; i < 16; i++) {
            int bt = getInt(i + "");
            bs[i] = (byte) bt;
        }
        if (bs[0] == 0 && bs[1] == 0 && bs[2] == 0 && bs[3] == 0 && bs[4] == 0 && bs[5] == 0)
            bs = null;
        return bs;
    }
}
