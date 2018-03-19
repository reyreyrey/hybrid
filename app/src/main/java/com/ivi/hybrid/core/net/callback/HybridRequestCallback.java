package com.ivi.hybrid.core.net.callback;

import android.text.TextUtils;

import com.ivi.hybrid.core.call.CallJS;
import com.ivi.hybrid.core.net.tools.ErrorMessage;
import com.ivi.hybrid.ui.progressDialog.ProgressDialogUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.smtt.sdk.WebView;

import static com.ivi.hybrid.core.net.tools.HybridRequestUrl.needChangeGatewayUrl;


/**
 * author: Rea.X
 * date: 2017/11/9.
 */

public class HybridRequestCallback extends StringCallback {
    private WebView webView;
    private String requestId;
    public HybridRequestCallback(){

    }

    public HybridRequestCallback(String requestId, WebView webView) {
        this.webView = webView;
        this.requestId = requestId;
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
        try {
            String result = response.body();
            ProgressDialogUtils.dismissProgress();
            if (webView != null && !TextUtils.isEmpty(requestId))
                CallJS.callJSnetCallback(webView, result, requestId);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<String> response) {
        super.onError(response);
        try {
            needChangeGatewayUrl();
            Throwable throwable = response.getException();
            String errorMsg = throwable.getMessage();
            LogUtil.e("网络请求失败：" + errorMsg);
            ProgressDialogUtils.dismissProgress();
            if (webView != null && !TextUtils.isEmpty(requestId))
                CallJS.callJSnetCallback(webView, JsonParse.toJson(ErrorMessage.getErrorModel(response.getException())), requestId);
        } catch (Throwable t) {
        }
    }
}
