package com.ivi.hybrid.core.modules.driver.agqj;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.reflect.TypeToken;
import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.progressDialog.ProgressDialogUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author: Rea.X
 * date: 2017/11/7.
 * AGQJ本地化
 */

public class LocalAGQJ {

    public static void toAGQJ(final PostWebView webView, String data) {
        ProgressDialogUtils.showProgress(webView.getContext());
        GameModel model = JsonParse.fromJson(data, GameModel.class);
        if (model == null) return;
        final String exParams = getExParams(data);
        String gameCode = model.getGameCode();
        Map<String, String> params = model.getParams();
        if (TextUtils.isEmpty(gameCode)) return;
        HybridRequest.request(params, model.isTry() ? "game/tryLoginInfoWithAGQJ" : "game/loginInfoWithAGQJ", new HybridRequestCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onSuccess(response);
                ProgressDialogUtils.dismissProgress();
                String result = response.body();
                InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                if (interceptModel == null || !interceptModel.isStatus()) {
                    ToastUtils.toastError(Hybrid.get(), interceptModel.getMessage() == null ? Hybrid.get().getResources().getString(R.string.hybrid_connect_timeout) : interceptModel.getMessage());
                    return;
                }
                ResultModel<String> m = JsonParse.fromJson(result, new TypeToken<ResultModel<String>>() {
                });
                if (m == null) return;
                String url = m.data;
                if (TextUtils.isEmpty(url)) return;
                url = URLDecoder.decode(url);
                url = getParams(Hybrid.get(), url, exParams);
                if (TextUtils.isEmpty(url)) return;
                url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
                url = "file:///android_asset/aggame/index.html?params=" + url;
                CallModule.callOutside(webView, url, "AGQJ");
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                super.onError(response);
                ProgressDialogUtils.dismissProgress();
                ToastUtils.toastError(Hybrid.get(), Hybrid.get().getString(R.string.hybrid_common_net_error));
            }
        });
    }

    @Nullable
    private static String getParams(Context context, String result, String exParams) {
        DomainHelper.DomainIP domainIP = DomainHelper.getCanUsedDomain();
        if (domainIP == null) {
            ToastUtils.toastError(context, context.getString(R.string.hybrid_no_host));
            return null;
        }
        String domain = domainIP.domain;
        if (TextUtils.isEmpty(domain)) {
            ToastUtils.toastError(context, context.getString(R.string.hybrid_no_host));
            return null;
        }
        String host = null;
        Uri url = Uri.parse(domain);
        host = url.getHost();
        if (TextUtils.isEmpty(host)) {
            ToastUtils.toastError(context, context.getString(R.string.hybrid_no_host));
            return null;
        }
        result += "&DomainName=" + host + (TextUtils.isEmpty(exParams) ? "" : exParams);
        LogUtil.d("------------->result----" + result);
        return result;
    }

    private static String getExParams(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Iterator<String> iterator = jsonObject.keys();
            StringBuffer sb = new StringBuffer();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.equals("gameCode") || key.equals("isTry") || key.equals("params")) continue;
                String value = jsonObject.getString(key);
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
