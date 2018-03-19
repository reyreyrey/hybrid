package com.ivi.hybrid.core.webview.x5.dns;

import android.os.Bundle;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * author: Rea.X
 * date: 2017/11/18.
 */

public class DNSClient extends WebViewClient {

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        WebResourceResponse webResourceResponse = DNSUtils.getDnsResponse(webView, s);
        if (webResourceResponse != null) return webResourceResponse;
        return super.shouldInterceptRequest(webView, s);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        WebResourceResponse webResourceResponse = DNSUtils.getDnsResponse(webView, webResourceRequest.getUrl().toString());
        if (webResourceResponse != null) return webResourceResponse;
        return shouldInterceptRequest(webView, webResourceRequest.getUrl().toString());
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
        WebResourceResponse webResourceResponse = DNSUtils.getDnsResponse(webView, webResourceRequest.getUrl().toString());
        if (webResourceResponse != null) return webResourceResponse;
        return shouldInterceptRequest(webView, webResourceRequest.getUrl().toString());
    }
}
