package com.ivi.hybrid.core.call;

import android.text.TextUtils;

import com.ivi.hybrid.core.bridge.AppBridge;
import com.ivi.hybrid.core.bridge.AppBridgeModel;
import com.ivi.hybrid.core.modules.forward.ForwardModel;
import com.ivi.hybrid.core.modules.forward.ForwardTools;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.utils.JsonParse;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class CallModule {

    public static void callOutside(String url) {
        ForwardModel model = new ForwardModel();
        model.setUrl(url);
        model.setNewView(true);
        ForwardTools.outside(null, model);
    }

    public static void callInside(String url) {
        ForwardModel model = new ForwardModel();
        model.setUrl(url);
        model.setNewView(true);
        ForwardTools.inside(null, model);
    }

    public static void callOutside(PostWebView webView, String url) {
        ForwardModel model = new ForwardModel();
        model.setUrl(url);
        model.setNewView(true);
        ForwardTools.outside(webView, model);
    }

    public static void callOutside(PostWebView webView, String url, String gameType) {
        ForwardModel model = new ForwardModel();
        model.setUrl(url);
        model.setGameType(gameType);
        model.setNewView(true);
        ForwardTools.outside(webView, model);
    }


    private static String invokeCache(String key, String value, String method) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cacheValue", value);
            map.put("value", jsonObject.toString());
        } catch (JSONException e) {
        }

        map.put("expire", -1);

        AppBridgeModel model = new AppBridgeModel();
        model.setRequestId(UUID.randomUUID().toString());
        model.setService("cache");
        model.setMethod(method);
        model.setData(map);

        AppBridge bridge = new AppBridge(null);
        return bridge.appInvoke(JsonParse.toJson(model));
    }

    /**
     * 调用语音客服
     *
     * @return
     */
    public static String invokeLive800(PostWebView webView) {
        String customerID = UserHelper.INSTANT.getUserId();
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", TextUtils.isEmpty(customerID) ? "0" : customerID);
        AppBridgeModel model = new AppBridgeModel();
        model.setRequestId(UUID.randomUUID().toString());
        model.setService("driver");
        model.setMethod("live800");
        model.setData(map);
        AppBridge bridge = new AppBridge(webView);
        try {
            return bridge.appInvoke(JsonParse.toJson(model));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 调用bridge的cache模块的get方法
     *
     * @param key
     * @return
     */
    public static String invokeCacheModuleGet(String key) {
        String result = invokeCache(key, "", "get");
        CacheResultModel model = JsonParse.fromJson(result, CacheResultModel.class);
        if (model == null) return null;
        String data = model.getData();
        if (TextUtils.isEmpty(data)) return "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            return jsonObject.getString("cacheValue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 调用bridge的cache模块的delete方法
     *
     * @param key
     */
    public static void invokeCacheModuleDelete(String key) {
        invokeCache(key, "", "delete");
    }

    /**
     * 调用bridge的cache模块的save方法
     *
     * @param key
     * @param value
     * @return
     */
    public static String invokeCacheModuleSave(String key, String value) {
        String result = invokeCache(key, value, "save");
        return result;
    }
}
