package com.ivi.hybrid.core.modules.ui;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.cons.Cons;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.progressDialog.ProgressDialogUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.ToastUtils;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import static com.ivi.hybrid.core.bridge.Bridge.returnJSData;

/**
 * author: Rea.X
 * date: 2017/5/16.
 * UI模块
 */

public class UiModule {

    public static String loading(String requestId, String data, PostWebView webView) {
        LoadingModel model = JsonParse.fromJson(data, LoadingModel.class);
        if (model == null) return null;
        if (model.getType().equals(Cons.Loading.SHOW)) {
            ProgressDialogUtils.showProgress(ActivityManager.getTagActivity());
        } else if (model.getType().equals(Cons.Loading.HIDE)) {
            ProgressDialogUtils.dismissProgress();
        }
        return returnJSData(requestId, true, "");
    }

    public static String toast(String requestId, String data, PostWebView webView) {
        ToastUtils.toastSuccess(Hybrid.get(), data);
        return returnJSData(requestId, true, "");
    }
}
