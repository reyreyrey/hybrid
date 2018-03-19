package com.ivi.hybrid.core.webview.x5;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hybrid.R;
import com.hybrid.databinding.HybridWebviewBinding;
import com.ivi.hybrid.core.webview.x5.clients.IDownloadListener;
import com.ivi.hybrid.core.webview.x5.clients.IWebChromeClient;
import com.ivi.hybrid.core.webview.x5.clients.IWebViewClient;
import com.ivi.hybrid.core.webview.x5.tools.IWebSetting;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class ProgressWebView extends RelativeLayout {
    private HybridWebviewBinding databinding;
    private IWebChromeClient chromeClient;
    private IWebViewClient webViewClient;


    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        databinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.hybrid_webview, this, true);
        IWebSetting.init(databinding.webview);
        chromeClient = new IWebChromeClient(this);
        webViewClient = new IWebViewClient(this);
        databinding.webview.setWebChromeClient(chromeClient);
        databinding.webview.setWebViewClient(webViewClient);
        databinding.webview.setDownloadListener(new IDownloadListener(getContext()));
    }

    public PostWebView getWebView() {
        return databinding.webview;
    }

    public ProgressBar getlineProgressbar() {
        return databinding.pbloading;
    }

    public ProgressBar getcircleProgressbar() {
        return databinding.pbCircleLoading;
    }

    public FrameLayout getfullFramlayout() {
        return databinding.fullFramlayout;
    }

    public void setIWebChromClient(IWebChromeClient chromClient) {
        databinding.webview.setWebChromeClient(chromClient);
    }

    public void setIWebViewClient(IWebViewClient client) {
        databinding.webview.setWebViewClient(client);
    }

    /**
     * 使用此webview，必须在Activity或者Fragment的onActivityResult回调中调用该方法，在super前调用，否则选择文件将不起效
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
    }
}
