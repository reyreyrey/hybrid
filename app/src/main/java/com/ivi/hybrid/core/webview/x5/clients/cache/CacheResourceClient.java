package com.ivi.hybrid.core.webview.x5.clients.cache;

import android.os.Bundle;

import com.ivi.hybrid.core.webview.x5.dns.DNSClient;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * author: Rea.X
 * date: 2017/11/17.
 */

public class CacheResourceClient extends DNSClient {

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        WebResourceResponse resourceResponse = CacheUtils.cache(webView, s);
        if (resourceResponse == null)
            return super.shouldInterceptRequest(webView, s);
        return resourceResponse;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        WebResourceResponse resourceResponse = CacheUtils.cache(webView, webResourceRequest.getUrl().toString());/**/
        if (resourceResponse == null)
            return super.shouldInterceptRequest(webView, webResourceRequest);
        return resourceResponse;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
        WebResourceResponse resourceResponse = CacheUtils.cache(webView, webResourceRequest.getUrl().toString());
        if (resourceResponse == null)
            return super.shouldInterceptRequest(webView, webResourceRequest, bundle);
        return resourceResponse;
    }


}
