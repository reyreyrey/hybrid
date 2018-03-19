package com.ivi.hybrid.core.bridge;

import android.text.TextUtils;

import com.ivi.hybrid.core.modules.cache.CacheModule;
import com.ivi.hybrid.core.modules.driver.DriverModule;
import com.ivi.hybrid.core.modules.forward.ForwardModule;
import com.ivi.hybrid.core.modules.net.NetModule;
import com.ivi.hybrid.core.modules.ui.UiModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * author: Rea.X
 * date: 2017/10/25.
 * 桥接帮助类
 */

public class Bridge {
    public static final String MODULE_CACHE_KEY = "cache";
    public static final String MODULE_DRIVER_KEY = "driver";
    public static final String MODULE_FORWARD_KEY = "forward";
    public static final String MODULE_NET_KEY = "net";
    public static final String MODULE_UI_KEY = "ui";


    private static Map<String, List<Class<?>>> regListMap = new HashMap<>();
    private static List<Class<?>> cacheList = new ArrayList<>();
    private static List<Class<?>> driverList = new ArrayList<>();
    private static List<Class<?>> forwardList = new ArrayList<>();
    private static List<Class<?>> netList = new ArrayList<>();
    private static List<Class<?>> uiList = new ArrayList<>();

    private static OnBridgeInvokedListener listener;


    /**
     * 初始化并注入预置的模块
     */
    static {
        regListMap.put(MODULE_CACHE_KEY, cacheList);
        regListMap.put(MODULE_DRIVER_KEY, driverList);
        regListMap.put(MODULE_FORWARD_KEY, forwardList);
        regListMap.put(MODULE_NET_KEY, netList);
        regListMap.put(MODULE_UI_KEY, uiList);

        cacheList.add(CacheModule.class);
        driverList.add(DriverModule.class);
        forwardList.add(ForwardModule.class);
        netList.add(NetModule.class);
        uiList.add(UiModule.class);

    }

    /**
     * 将module注册进hash表
     * @param key
     * @param cls
     */
    public static void regModule(String key, Class<?> cls) {
        if (key == null || key.length() == 0 || cls == null) return;
        List<Class<?>> list = regListMap.get(key);
        if (list != null && !list.contains(cls)) {
            list.add(cls);
        } else {
            List<Class<?>> clazzList = new ArrayList<>();
            regListMap.put(key, clazzList);
            clazzList.add(cls);
        }
    }


    public static void unRegModule(String key, Class<?> cls) {
        if (key == null || key.length() == 0 || cls == null) return;
        List<Class<?>> list = regListMap.get(key);
        if (list != null && list.contains(cls)) {
            list.remove(cls);
        }
    }

    /**
     * 获取注册的module类
     *
     * @param key
     * @param method
     * @return
     */
    public static Class<?> getClassByMethod(String key, String method) {
        if (key == null || key.length() == 0 || method == null) return null;
        List<Class<?>> list = regListMap.get(key);
        if (list != null || list.size() > 0) {
            for(Class cls : list) {
                Method[] methods = cls.getMethods();
                for(Method mtd : methods) {
                    if (mtd != null && mtd.getName().equals(method)) {
                        return cls;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 设置桥接调用监听器
     *
     * @param listener
     */
    public static void setOnBridgeInvokedListener(OnBridgeInvokedListener listener) {
        Bridge.listener = listener;
    }

    static OnBridgeInvokedListener getListener() {
        return listener;
    }

    public static String returnJSData(String requestId, boolean status, String data) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", requestId);
            jsonObject.put("status", status);
            jsonObject.put("data", data);
            return jsonObject.toString();
        } catch (JSONException e) {
        }
        return null;
    }

}
