package com.ivi.hybrid.core.webview.x5.clients;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.webview.x5.ProgressWebView;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ivi.hybrid.core.webview.x5.tools.WebTools.checkIsGame;

/**
 * author: Rea.X
 * date: 2017/8/8.
 */

public class IWebChromeClient extends IWebChromeFileClient {
    private WebView webView;
    private FrameLayout fullFramlayout;
    private ProgressBar lineProgressbar;
    private ProgressBar circleProgressbar;
    private IX5WebChromeClient.CustomViewCallback callback;
    private boolean isShowLineProgress, isShowCircleProgress;


    public IWebChromeClient(ProgressWebView view) {
        super(view);
        this.webView = view.getWebView();
        this.fullFramlayout = view.getfullFramlayout();
        this.lineProgressbar = view.getlineProgressbar();
        this.circleProgressbar = view.getcircleProgressbar();
        this.isShowLineProgress = Config.isShowLineLoading();
        this.isShowCircleProgress = Config.isShowCircleLoading();
    }


    @Override
    public void onProgressChanged(WebView webView, int newProgress) {
        super.onProgressChanged(webView, newProgress);
        if (isShowLineProgress && !checkIsGame(webView.getUrl())) {
            this.lineProgressbar.setVisibility(View.VISIBLE);
            this.lineProgressbar.setProgress(newProgress);
        }
        if (newProgress >= 80) {
            this.circleProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n|");
        sb.append("|------------------------------------------------------------------------------------------------------------------|");
        sb.append("\n|");
        sb.append("|\tmessage->" + consoleMessage.message());
        sb.append("\n|");
        sb.append("|\tsourceId->" + consoleMessage.sourceId());
        sb.append("\n|");
        sb.append("|\tlineNumber->" + consoleMessage.lineNumber());
        sb.append("\n|");
        sb.append("|\tmessageLevel->" + consoleMessage.messageLevel());
        sb.append("\n|");
        sb.append("|----------------------------------------------------------------------------------------------------------------|");
        switch (consoleMessage.messageLevel()) {
            case ERROR:
                LogUtil.e("consoleMessage:" + sb.toString());
                break;
            case WARNING:
                LogUtil.w("consoleMessage:" + sb.toString());
                break;
            default:
                LogUtil.d("consoleMessage:" + sb.toString());
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onHideCustomView() {
        if (callback != null) {
            callback.onCustomViewHidden();
        }
        webView.setVisibility(View.VISIBLE);
        fullFramlayout.removeAllViews();
        fullFramlayout.setVisibility(View.GONE);
        super.onHideCustomView();
    }


    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        webView.setVisibility(View.GONE);
        fullFramlayout.setVisibility(View.VISIBLE);
        fullFramlayout.removeAllViews();
        fullFramlayout.addView(view);
        callback = customViewCallback;
        super.onShowCustomView(view, customViewCallback);
    }

    @Override
    public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        webView.setVisibility(View.GONE);
        fullFramlayout.setVisibility(View.VISIBLE);
        fullFramlayout.removeAllViews();
        fullFramlayout.addView(view);
        callback = customViewCallback;
        super.onShowCustomView(view, i, customViewCallback);
    }
}
