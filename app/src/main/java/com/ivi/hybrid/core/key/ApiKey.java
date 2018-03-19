package com.ivi.hybrid.core.key;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Rea.X on 2017/2/3.
 */

public class ApiKey {
    static {
        System.loadLibrary("apiKey");
    }

    public static native String getAppKey(Object context, int model);

    public static native String getAppId(Object context);


}
