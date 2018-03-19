package com.ivi.hybrid.core.modules.net;

import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.progressDialog.ProgressDialogUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;

import static com.ivi.hybrid.core.bridge.Bridge.returnJSData;

/**
 * author: Rea.X
 * date: 2017/5/16.
 * 网络模块
 */

public class NetModule {

    public String invoke(String requestId, String data, PostWebView webView) {
        LogUtil.d("js调用app网络模块：requestid->" + requestId + "  数据：" + data);
        NetParamsModel model = JsonParse.fromJson(data, NetParamsModel.class);
        if (model == null) return null;
        if (model.isShowLoading()) {
            ProgressDialogUtils.showProgress(webView.getContext());
        }
        HybridRequest.request(webView, requestId, model.getParams(), model.getApiUrl());
        return returnJSData(requestId, true, "");
    }


}
