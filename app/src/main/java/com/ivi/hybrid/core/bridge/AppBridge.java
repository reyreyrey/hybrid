package com.ivi.hybrid.core.bridge;

import android.webkit.JavascriptInterface;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.exception.BridgeException;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/10/25.
 * 桥接类
 */

public class AppBridge {

    private OnBridgeInvokedListener listener;
    private PostWebView webView;

    public AppBridge(PostWebView webView) {
        listener = Bridge.getListener();
        this.webView = webView;
    }

    @JavascriptInterface
    public String appInvoke(String json) {
        LogUtil.d("js调用app:" + json);
        AppBridgeModel model = JsonParse.fromJson(json, AppBridgeModel.class);
        if(model == null)return null;
        if (listener != null) {
            listener.onBeforeInvoked(model);
        }
        String result = invoke(model);
        if (listener != null) {
            listener.onAfterInvoked(model, result);
        }
        return result;
    }


    private String invoke(AppBridgeModel model) {
        if (model == null)
            throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_parse_appbridge_error));
        Class<?> cls = Bridge.getClassByMethod(model.getService(), model.getMethod());
        if (cls == null)
            throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_can_not_find_mudule));
        Method method;
        try {
            method = cls.getMethod(model.getMethod(), String.class, String.class, PostWebView.class);
        } catch (NoSuchMethodException e) {
            throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_can_not_find_method));
        }
        method.setAccessible(true);
        try {
            Map<String, Object> data = model.getData();
            JSONObject json = new JSONObject();
            if (data != null) {
                json = new JSONObject(data);
            }
            return (String) method.invoke(cls.newInstance(), model.getRequestId(), json.toString(), webView);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException exception = (InvocationTargetException) e;
                Throwable throwable = exception.getTargetException();
                LogUtil.e("AppBridge error:" + throwable.getMessage() + "   model:::" + model.toString());
            }
            throw new BridgeException(String.format(Hybrid.getContext().getString(R.string.hybrid_can_not_invoke_method), e.toString()));
        }
    }
}
