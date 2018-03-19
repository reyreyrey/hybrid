package com.ivi.hybrid.core.net.tools;

import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.PingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: Rea.X
 * date: 2017/11/1.
 */

public class HybridRequestUrl {
    private static volatile String baseUrl = "";
    private static List<String> gatewayUrls;
    private static final String GATEWAY_BASEURL_KEY = "gateway_base_url";
    static {
        baseUrl = CallModule.invokeCacheModuleGet(GATEWAY_BASEURL_KEY);
        gatewayUrls = new ArrayList<>(Arrays.asList(Config.getBaseurl()));
    }

    private static String ping() {
        String randomUrl = gatewayUrls.get(0);
        while (gatewayUrls.size() != 0) {
            int random = (int) (Math.random() * gatewayUrls.size());
            randomUrl = gatewayUrls.remove(random);
            int result = PingUtils.getAvgRTT(randomUrl, 1, 1);
            if (result == -1 && gatewayUrls.size() != 0) {
                continue;
            }
            return randomUrl;
        }
        return randomUrl;
    }

    public static void needChangeGatewayUrl(){
        baseUrl = ping();
        saveBaseUrlToLocal();
    }

    public static String getRequestUrl(String path) {
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = ping();
            saveBaseUrlToLocal();
        }
        if (!TextUtils.isEmpty(baseUrl) && baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        LogUtil.d("gatewap网关地址：" + baseUrl);
        path = path.trim();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return baseUrl + "/" + path/* + "?XDEBUG_SESSION_START"*/;
    }

    private static void saveBaseUrlToLocal(){
        if(Hybrid.isDebug()){
            CallModule.invokeCacheModuleSave(GATEWAY_BASEURL_KEY, "");
            return;
        }
        CallModule.invokeCacheModuleSave(GATEWAY_BASEURL_KEY, baseUrl);
    }
}
