package com.ivi.hybrid.core.modules.forward;

import android.text.TextUtils;

import com.ivi.hybrid.core.call.CallJS;
import com.ivi.hybrid.core.net.tools.ErrorMessage;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.progressDialog.ProgressDialogUtils;
import com.ivi.hybrid.utils.JsonParse;

import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ivi.hybrid.core.bridge.Bridge.returnJSData;

/**
 * author: Rea.X
 * date: 2017/5/16.
 * 跳转模块
 */

public class ForwardModule {

    /**
     * 跳转到内部url
     *
     * @param requestId
     * @param data
     */
    public String inside(String requestId, String data, final PostWebView webView) {
        final ForwardModel model = JsonParse.fromJson(data, ForwardModel.class);
        if (model == null) return null;
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                MainThreadDisposable.verifyMainThread();
                ForwardTools.inside(webView, model);
            }
        });

        return returnJSData(requestId, true, "");
    }


    /**
     * 跳转到外部url
     *
     * @param requestId
     * @param data
     */
    public String outside(String requestId, String data, final PostWebView webView) {
        final ForwardModel model = JsonParse.fromJson(data, ForwardModel.class);
        if (model == null) return null;
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                MainThreadDisposable.verifyMainThread();
                ForwardTools.outside(webView, model);
            }
        });
        return returnJSData(requestId, true, "");
    }


}
