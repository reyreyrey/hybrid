package com.ivi.hybrid.ui.clients;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.ivi.hybrid.core.call.CallJS;
import com.ivi.hybrid.core.cons.Cons;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.webview.x5.ProgressWebView;
import com.ivi.hybrid.core.webview.x5.clients.IWebViewClient;
import com.ivi.hybrid.ui.activitys.HybridMainActivity;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.net.URLDecoder;

/**
 * author: Rea.X
 * date: 2017/11/6.
 */

public class LoadFinishWebClient extends IWebViewClient {
    private TextView titleTextView;
    private boolean isFirstPage;

    public LoadFinishWebClient(ProgressWebView view, boolean isFirst) {
        super(view);
        this.isFirstPage = isFirst;
    }

    public LoadFinishWebClient(ProgressWebView view, TextView titleTextView) {
        super(view);
        this.titleTextView = titleTextView;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtil.d("onPageFinished:" + url);
        Context context = view.getContext();
        if (DomainHelper.isWebHost(url) || DomainHelper.isNativalDomain(url)) {
            CallJS.loadFinish(view);
        }
        if (context != null && (context instanceof HybridMainActivity)) {
            HybridMainActivity activity = (HybridMainActivity) context;
            activity.preloadFinish(isFirstPage);
        }
        if (titleTextView != null) {
            receiverTitle(view);
        }
    }


    /**
     * 获取标题
     *
     * @param view WebView
     */
    private void receiverTitle(WebView view) {
        String s = "javascript:(function (d) {var e = d.getElementById(\"proTitle\");return e?\"\"+new String(e.innerHTML) : \"\";})(document);";
        view.evaluateJavascript(s, new ValueCallback<String>() {

            public void onReceiveValue(String s) {
                if (!TextUtils.isEmpty(s) && !s.equals("null")) {
                    try {
                        s = URLDecoder.decode(s);
                        s = s.replaceAll("\"", "").replaceAll("\"", "").replaceAll(" ", "").replaceAll("\\\\n", "");
                    } catch (Exception e) {
                        System.out.println("");
                    }
                } else {
                    s = "";
                }
                titleTextView.setText(s);
            }
        });
    }
}
