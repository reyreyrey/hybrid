package com.ivi.hybrid.core.webview.x5.tools;

import android.content.Context;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.CommonUtils;
import com.tencent.smtt.sdk.WebView;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class UserAgent {

    private static final String DEFAULT_USERAGENT = "Mozilla/5.0 (Linux; Android 6.0.1; SM-T815Y Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.105 Safari/537.36";

    public static String getUserAgent(WebView webview) {
        String defaultAgent = webview.getSettings().getUserAgentString();
        String great = " app_version=" + CommonUtils.getVersionName(Hybrid.getContext()) + " great-winner,Mobile";
        return defaultAgent.contains(great) ? defaultAgent : defaultAgent + great;
    }

}
