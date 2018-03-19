package com.ivi.hybrid.core.webview.x5.dns;

import android.webkit.JavascriptInterface;

import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.bridge.AppBridge;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;

import static com.ivi.hybrid.core.webview.x5.dns.DNSUtils.LOG_TAG;


/**
 * author: Rea.X
 * date: 2017/9/26.
 */

public class PostInterceptJavascriptInterface extends AppBridge {
    public PostInterceptJavascriptInterface(PostWebView webView) {
        super(webView);
    }

    @JavascriptInterface
    public void formPost(String s) {
        LogUtil.d(LOG_TAG + "receiver formData: " + s);
        PostFormDataModel model = JsonParse.fromJson(s, new TypeToken<PostFormDataModel>() {
        }.getType());
        if (model == null) return;
        PostDataCacheUtils.save(model.getUrl(), model.getData());
    }

    @JavascriptInterface
    public void ajaxPost(String s) {
        LogUtil.d(LOG_TAG + "receiver ajaxPost: " + s);
        PostAjaxDataModel model = JsonParse.fromJson(s, new TypeToken<PostAjaxDataModel>() {
        }.getType());
        if (model == null) return;
        PostDataCacheUtils.save(model.getUrl().getUrl(), model.getUrl().getData());
    }
}
