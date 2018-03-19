package com.ivi.hybrid.core.net;


import android.app.Activity;

import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.client.ClientManager;
import com.ivi.hybrid.core.net.tools.Sign;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.tencent.smtt.sdk.WebView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

import static com.ivi.hybrid.core.net.tools.HybridRequestUrl.getRequestUrl;

/**
 * author: Rea.X
 * date: 2017/11/1.
 * 网络请求类
 */

public class HybridRequest {


    public static void request(WebView webView, String requestId, Map<String, String> params, String path) {
        doPost(path, params, new HybridRequestCallback(requestId, webView));
    }

    public static void request(Map<String, String> params, String path, HybridRequestCallback callback) {
        doPost(path, params, callback);
    }


    public static Response request(Map<String, String> params, String path) throws IOException {
        return buildRequest(params, path).execute();
    }

    private static Request<String, PostRequest<String>> buildRequest(Map<String, String> params, String path) {
        if (params == null)
            params = new HashMap<>();
        Activity activity = ActivityManager.getTagActivity();
        String tag = "";
        if (activity != null && activity instanceof HybridWebViewActivity) {
            tag = HybridWebViewActivity.class.getName();
        }
        String url = getRequestUrl(path);
        return OkGo
                .<String>post(url)
                .params(Sign.getParams(params))
                .client(ClientManager.getDefaultHttpClient())
                .tag(tag);
    }

    private static void doPost(String path, Map<String, String> hMap, HybridRequestCallback callback) {
        buildRequest(hMap, path).execute(callback);
    }
}
