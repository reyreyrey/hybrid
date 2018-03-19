package com.ivi.hybrid.core.webview.x5.clients;

/**
 * author: Rea.X
 * date: 2017/4/7.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.webview.x5.ProgressWebView;
import com.ivi.hybrid.core.webview.x5.clients.cache.CacheResourceClient;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;

import static com.ivi.hybrid.core.webview.x5.tools.WebTools.checkIsGame;

public class IWebViewClient extends CacheResourceClient {
    protected ProgressWebView view;
    protected Context context;
    protected ProgressBar lineProgressBar;
    protected ProgressBar circleProgressbar;
    protected boolean isShowLineProgress, isShowCircleProgress;

    public IWebViewClient(ProgressWebView view) {
        this.view = view;
        this.context = view.getContext();
        this.lineProgressBar = view.getlineProgressbar();
        this.circleProgressbar = view.getcircleProgressbar();
        this.isShowLineProgress = Config.isShowLineLoading();
        this.isShowCircleProgress = Config.isShowCircleLoading();
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (isShowLineProgress && !checkIsGame(url)) {
            this.lineProgressBar.setVisibility(View.VISIBLE);
            this.lineProgressBar.setProgress(0);
        }
        if (isShowCircleProgress && !checkIsGame(url))
            this.circleProgressbar.setVisibility(View.VISIBLE);
        LogUtil.d("onPageStarted->" + url);

    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        sslErrorHandler.proceed();
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        LogUtil.d("shouldOverrideUrlLoading:" + s);
        if (!TextUtils.isEmpty(s) && s.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(s));
            webView.getContext().startActivity(intent);
            return true;
        }

        if (TextUtils.isEmpty(s)) return false;
        if (s.contains("javascript: void(0)") || s.contains("javascript:void(0)")) return false;
        if (isShowLineProgress && !checkIsGame(s)) {
            this.lineProgressBar.setVisibility(View.VISIBLE);
            this.lineProgressBar.setProgress(0);
        }
        if (isShowCircleProgress && !checkIsGame(s))
            this.circleProgressbar.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(s);

        //try{} => catch{} added by Barry
        String schem;
        try {
            schem = uri.getScheme();
        } catch (UnsupportedOperationException e) {
            return false;
        }


        if (schem != null && schem.contains("file")) return false;
        if (schem != null && (schem.contains("http") || schem.contains("https"))) {
            if (DomainHelper.isWebHost(s)) {
                webView.loadUrl(s);
                return true;
            }
            return false;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                webView.getContext().startActivity(intent);
                Context context = webView.getContext();
                if(context instanceof Activity){
                    Activity activity = (Activity) context;
                    if(activity instanceof HybridWebViewActivity){
                        activity.onBackPressed();
                    }
                }
            } catch (Exception e) {
                LogUtil.e(e.toString());
            }
        }

        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.circleProgressbar.setVisibility(View.GONE);
        this.lineProgressBar.setVisibility(View.GONE);
    }
}
