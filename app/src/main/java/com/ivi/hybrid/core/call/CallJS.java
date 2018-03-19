package com.ivi.hybrid.core.call;

import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Response;

/**
 * author: Rea.X
 * date: 2017/11/1.
 * 调用js工具类
 */

public class CallJS {
    private static final String JS_CALLBACK = "javascript: window.JSCallback && window.JSCallback(%1$s);";

    /**
     * 回调js网络模块
     *
     * @param webView
     * @param result
     * @param requestId
     */
    public static void callJSnetCallback(WebView webView, String result, String requestId) {
        callJS(webView, requestId, "callback", result);
    }

    /**
     * 回调js网络模块
     *
     * @param webView
     * @param response
     * @param requestId
     */
    public static void callJSnetCallback(WebView webView, Response response, String requestId) {
        try {
            callJS(webView, requestId, "callback", response.body().string());
        } catch (IOException e) {
            LogUtil.e("回调JS报错........"+e.toString());
        }
    }

    /**
     * APP调用js
     *
     * @param webView
     * @param model
     */
    public static void callJS(WebView webView, CallJSModel model) {
        callJS(webView, model, null);
    }

    public static void callJS(final WebView webView, CallJSModel model, final ValueCallback callback) {
        try {
            final String js = String.format(JS_CALLBACK, JsonParse.toJson(model));
            webView.evaluateJavascript(js, callback);
        } catch (Throwable w) {

        }
    }

    public static void callJS(WebView webView, String requestId, String method, String data) {
        CallJSModel model = new CallJSModel();
        model.setRequestId(requestId);
        model.setMethod(method);
        model.setData(data);
        callJS(webView, model);
    }

    public static void callJS(WebView webView, String method, String data) {
        CallJSModel model = new CallJSModel();
        model.setRequestId(UUID.randomUUID().toString());
        model.setMethod(method);
        model.setData(data);
        callJS(webView, model);
    }

    public static void callJS(WebView webView, String method) {
        CallJSModel model = new CallJSModel();
        model.setRequestId(UUID.randomUUID().toString());
        model.setMethod(method);
        callJS(webView, model);
    }

    public static void loadFinish(WebView webView) {
        callJS(webView, "loadFinish");
    }
}
