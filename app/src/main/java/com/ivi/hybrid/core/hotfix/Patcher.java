package com.ivi.hybrid.core.hotfix;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.callback.DownloadCallback;
import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.download.DownLoad;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.lzy.okgo.model.Response;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Patcher {

    private Patcher() {
    }

    public static void apply() {
        Map<String, String> params = new HashMap<>();
        params.put("app_version", CommonUtils.getVersionName(Hybrid.get()));
        params.put("version", getPatcherVersion());
        HybridRequest.request(params, "app/patchHotUpdate", new HybridRequestCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onSuccess(response);
                String result = response.body();
                InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                if (interceptModel == null || !interceptModel.isStatus()) {
                    return;
                }

                final ResultModel<PatcherModel> r = JsonParse.fromJson(
                        result,
                        new TypeToken<ResultModel<PatcherModel>>() {
                        }.getType()
                );

                if (r != null && r.status && !TextUtils.isEmpty(r.data.url) && !TextUtils.isEmpty(r.data.version)) {
                    String newName = CommonUtils.getNameFromUrl(r.data.url);
                    if (newName.contains(".apk")) {
                        newName = newName.replaceAll(".apk", r.data.version + ".apk");
                    } else if (newName.contains(".zip")) {
                        newName = newName.replace(".zip", r.data.version + ".zip");
                    } else {
                        throw new UnsupportedOperationException();
                    }

                    if (CallModule.invokeCacheModuleGet(getPatcherKey()).equals(r.data.version)) {
                        return;
                    }

                    File apk = new File(CommonUtils.getCacheDir(Hybrid.get()), newName);
                    if (apk.exists()) {
                        TinkerInstaller.onReceiveUpgradePatch(Hybrid.get(), apk.getAbsolutePath());
                        CallModule.invokeCacheModuleSave(getPatcherKey(), r.data.version);
                        return;
                    }

                    DownLoad.INSTANT.download(r.data.url, newName, new DownloadCallback() {
                        @Override
                        public void requestSuccess(File file) {
                            TinkerInstaller.onReceiveUpgradePatch(Hybrid.get(), file.getAbsolutePath());
                            CallModule.invokeCacheModuleSave(getPatcherKey(), r.data.version);
                        }

                        @Override
                        public void requestFial() {
                        }

                        @Override
                        public void downProgress(long readLength, long contentLength,
                                                 int progress) {
                        }
                    }, false);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    private static String getPatcherKey() {
        return "PatcherVersion" + CommonUtils.getVersionName(Hybrid.get());
    }

    public static String getPatcherVersion() {
        String version = CallModule.invokeCacheModuleGet(getPatcherKey());
        if (TextUtils.isEmpty(version)) {
            CallModule.invokeCacheModuleSave(getPatcherKey(), "0.0.0");
            return "0.0.0";
        }
        return version;
    }

    public static class PatcherModel {
        @SerializedName("version")
        public String version;
        @SerializedName("download_url")
        public String url;
    }
}
