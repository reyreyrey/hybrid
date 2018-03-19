package com.ivi.hybrid.core.push.helper;


import com.ivi.hybrid.core.push.utils.Utils;

/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class UUIDUtils {

    /**
     * uuid是否可用
     * @param t
     * @return
     */
    public static boolean isUsedUUID(byte [] t){
        byte[] uuidBytes = getUUIDBytes(t);
        int iUUID = Utils.byteArrayToInt(uuidBytes);
        return iUUID != 0;
    }

    /**
     * 获取uuid
     * @param t
     * @return
     */
    public static byte [] getUUIDBytes(byte [] t){
        byte[] uuidBytes = new byte[16];
        for (int i = 3; i < 19; i++) {
            uuidBytes[i - 3] = t[i];
        }
        return uuidBytes;
    }

    /**
     * 获取包的第一个字节的第二位是不是等于1(判断是不是uuid包)
     * @param b1
     * @return
     */
    public static boolean checkIsUUIDPackage(byte b1) {
        byte b = (byte)(b1 & 0xff);
        byte [] bits = Utils.byte2byteArray(b);
        return bits[1] == 1;
    }
}
