package com.ivi.hybrid.core.webview.x5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.utils.EncodingUtils;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

import static com.ivi.hybrid.core.webview.x5.tools.WebTools.getUserParamsGetMethod;
import static com.ivi.hybrid.core.webview.x5.tools.WebTools.urlDeleteUserMsg;


/**
 * author: Rea.X
 * date: 2017/9/23.
 */

public class PostWebView extends WebView {
    public PostWebView(Context context) {
        super(context);
    }

    private OnScrollChangedCallback callback;

    public PostWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PostWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        removeSearchBoxJavaBridgeInterface();
    }

    public PostWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        removeSearchBoxJavaBridgeInterface();
    }

    public PostWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        removeSearchBoxJavaBridgeInterface();
    }

    @SuppressLint("NewApi")
    private void removeSearchBoxJavaBridgeInterface() {
        try{
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            }
        }catch (Exception e){}
    }
    @Override
    public void loadUrl(String url) {
        loadUrl(url, null);
    }

    @Override
    public void loadUrl(String url, Map<String, String> map) {
        String[] getwayUrls = Config.getBaseurl();
        for (String s : getwayUrls) {
            if (s.equalsIgnoreCase(url)) {
                ActivityManager.getTagActivity().finish();
                return;
            }
        }
        Uri uri = Uri.parse(url);
        if (uri == null) {
            super.loadUrl(url, map);
            return;
        }
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) {
            super.loadUrl(url, map);
            return;
        }
        if (scheme.contains("file")) {
            super.loadUrl(url, map);
            return;
        }
        if (DomainHelper.isWebHost(url)) {
            url = urlDeleteUserMsg(url);
            postUrl(url, EncodingUtils.getBytes(getUserParamsGetMethod(), "utf-8"));
            return;
        }
        super.loadUrl(url, map);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null) {
            callback.onScroll(l, oldl, t, oldt);
        }
    }


    public interface OnScrollChangedCallback {
        void onScroll(int x, int oldx, int y, int oldy);
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback callback) {
        this.callback = callback;
    }


}
