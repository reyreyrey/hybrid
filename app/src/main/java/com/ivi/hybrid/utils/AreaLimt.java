package com.ivi.hybrid.utils;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.modules.forward.WebViewBundleModel;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;

import java.util.HashMap;
import java.util.Map;

import static com.ivi.hybrid.core.domain.DomainHelper.checkAndAddHost;

/**
 * author: Rea.X
 * date: 2017/11/17.
 */

public class AreaLimt {

    public static void checkAreaLimit() {
        if (!Config.isOpenAreaLimit()) return;
        Map<String, String> params = new HashMap<>();
        params.put("domain", DomainHelper.getCanUsedDomain().domain);
        HybridRequest.request(params, "app/areaLimit", new HybridRequestCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onSuccess(response);
                String result = response.body();
                if (!TextUtils.isEmpty(result)) {
                    InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                    if (interceptModel != null && interceptModel.status) {
                        ResultModel<String> model = JsonParse.fromJson(result, new TypeToken<ResultModel<String>>() {
                        }.getType());
                        if (model != null) {
                            String data = model.data;
                            if (!TextUtils.isEmpty(data)) {
                                if (!data.startsWith("http://") && !data.startsWith("https://")) {
                                    data = checkAndAddHost(data);
                                    WebViewBundleModel bundleModel = new WebViewBundleModel(data, false, -1, false, -1, false);
                                    HybridWebViewActivity.openWebView(bundleModel, false);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
