package com.ivi.hybrid.utils;

import android.os.Build;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.ValueCallback;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class Cookie {
    public static void clearCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.removeAllCookies((ValueCallback) null);
        } else {
            cookieManager.removeAllCookie();
        }

    }
}
