package com.ivi.hybrid.core.modules.cache;

import android.app.Activity;

import com.ivi.hybrid.core.bridge.Bridge;
import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.activitys.UIActivity;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ivi.hybrid.core.bridge.Bridge.returnJSData;
import static com.ivi.hybrid.core.cons.Cons.CUSTOMER_KEY;
import static com.ivi.hybrid.utils.Cookie.clearCookie;


/**
 * author: Rea.X
 * date: 2017/5/16.
 * 缓存模块
 */

public class CacheModule {
    private Map<String, String> cacheMap;

    public CacheModule() {
        cacheMap = new HashMap<>();
    }

    public String save(String requestId, String data, PostWebView webView) {
        CacheModel model = JsonParse.fromJson(data, CacheModel.class);
        if (model == null) return null;
        LogUtil.d("cache save::::" + model.toString());
        long expire = model.getExpire();
        if (expire == 0) {
            //存内存
            cacheMap.put(model.getKey(), model.getValue());
        } else {
            //存缓存
            PreferencesHelper.saveString(model.getKey(), model.getValue());
        }
        if (model.getKey().equals(CUSTOMER_KEY)) {
            Push.getInstance().loginStatusChanged(true);
            List<Activity> activitys = ActivityManager.getStackActivitys();
            for (Activity activity : activitys) {
                if (activity instanceof UIActivity) {
                    final UIActivity uiActivity = (UIActivity) activity;
                    uiActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uiActivity.onLoginStatusChanged(true);
                        }
                    });
                }
            }
        }
        return returnJSData(requestId, true, "");
    }


    public String update(String requestId, String data, PostWebView webView) {
        return save(requestId, data, webView);
    }

    public String delete(String requestId, String data, PostWebView webView) {
        CacheModel model = JsonParse.fromJson(data, CacheModel.class);
        if (model == null) return null;
        PreferencesHelper.delete(model.getKey());
        if (cacheMap.containsKey(model.getKey())) {
            cacheMap.remove(model.getKey());
        }
        if (model.getKey().equals(CUSTOMER_KEY)) {
            clearCookie();
            Push.getInstance().loginStatusChanged(false);
            List<Activity> activitys = ActivityManager.getStackActivitys();
            for (Activity activity : activitys) {
                if (activity instanceof UIActivity) {
                    final UIActivity uiActivity = (UIActivity) activity;
                    uiActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uiActivity.onLoginStatusChanged(false);
                        }
                    });
                }
            }
        }
        return returnJSData(requestId, true, "");
    }

    public String clear(String requestId, String data, PostWebView webView) {
        //空实现，不允许清除全部缓存
        return returnJSData(requestId, true, "");
    }

    public String get(String requestId, String data, PostWebView webView) {
        CacheModel model = JsonParse.fromJson(data, CacheModel.class);
        if (model == null) return null;
        if (cacheMap.containsKey(model.getKey())) {
            String value = cacheMap.get(model.getKey());
            return returnJSData(requestId, true, value);
        }
        String value = PreferencesHelper.getString(model.getKey());
        if (value == null)
            value = "";
        LogUtil.d("cache module:: get::key:" + model.getKey() + "  value:" + value);
        return returnJSData(requestId, true, value);
    }

}
