package com.ivi.hybrid.core.unzip;

/**
 * author: Rea.X
 * date: 2017/11/4.
 */

public class Unzip {
    static {
        System.loadLibrary("zip");
    }

    public static native int unzip(String dstPath, String zipFile);
}
