package com.ivi.hybrid.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hybrid.R;
import com.hybrid.databinding.HybridFragmentBinding;
import com.ivi.hybrid.core.bridge.AppBridge;
import com.ivi.hybrid.core.modules.forward.ForwardTools;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.core.webview.x5.ProgressWebView;
import com.ivi.hybrid.core.webview.x5.clients.IWebViewClient;
import com.ivi.hybrid.core.webview.x5.dns.PostInterceptJavascriptInterface;
import com.ivi.hybrid.ui.clients.LoadFinishWebClient;

import java.util.UUID;

import static com.ivi.hybrid.core.call.CallJS.loadFinish;
import static com.ivi.hybrid.core.cons.Cons.FILE_URL_BEFORE;
import static com.ivi.hybrid.core.cons.Cons.HTML_FILE_PATH;
import static com.ivi.hybrid.core.webview.x5.tools.WebTools.releaseWebView;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class HybridWebViewFragment extends BaseFragment<HybridFragmentBinding> {

    private ProgressWebView progressWebView;
    private PostWebView webView;

    public String url;
    private boolean isFristPage;

    public static HybridWebViewFragment get(String url, boolean isFristPage) {
        ForwardTools.add(url);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isFristPage", isFristPage);
        HybridWebViewFragment fragment = new HybridWebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    public PostWebView getWebView() {
        return webView;
    }

    public ProgressWebView getProgressWebView() {
        return progressWebView;
    }

    @Override
    protected int getContentView() {
        return R.layout.hybrid_fragment;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        url = bundle.getString("url");
        isFristPage = bundle.getBoolean("isFristPage", false);
        progressWebView = binding.webview;
        webView = binding.webview.getWebView();
        progressWebView.setIWebViewClient(new LoadFinishWebClient(progressWebView, isFristPage));
        PostInterceptJavascriptInterface bridge = new PostInterceptJavascriptInterface(webView);
        webView.addJavascriptInterface(bridge, "appClient");
        loadUrl();
    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(url)) {
            webView.setTag(url);
            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file:///")) {
                url = FILE_URL_BEFORE + HTML_FILE_PATH + url;
            }
            webView.loadUrl(url);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWebView(webView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressWebView.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void lzayLoadEveryVisible() {
        super.lzayLoadEveryVisible();
        loadFinish(webView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded() && isVisible)
            loadFinish(webView);
    }
}
