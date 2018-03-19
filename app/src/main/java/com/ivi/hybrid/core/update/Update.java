package com.ivi.hybrid.core.update;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.modules.driver.ChannelModel;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.callback.DownloadCallback;
import com.ivi.hybrid.core.net.callback.HybridRequestCallback;
import com.ivi.hybrid.core.net.download.DownLoad;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.unzip.Unzip;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.MD5;
import com.ivi.hybrid.utils.VersionTools;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.ivi.hybrid.core.cons.Cons.COPY_FILE_PATH;
import static com.ivi.hybrid.core.update.UpdateProgressDialog.FORCE_UPDATE;
import static com.ivi.hybrid.core.update.UpdateProgressDialog.NO_FORCE_UPDATE;

/**
 * author: Rea.X
 * date: 2017/11/13.
 */

public class Update {

    private static final String UPDATE_MODEL = "update_model";
    private static boolean isShowed;
    private static VersionModel versionModel;

    public static void startCheck() {
        Map<String, String> params = new HashMap<>();
        params.put("version", CommonUtils.getVersionName(Hybrid.get()));
        params.put("delta_version", VersionTools.getDeltaVersion());
        String parentId = Hybrid.getChannelID();
        params.put("channelId", TextUtils.isEmpty(parentId) ? "" : parentId);
        HybridRequest.request(params, "app/versionUpdate", new HybridRequestCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                super.onSuccess(response);
                String result = response.body();
                InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                if (interceptModel == null || !interceptModel.isStatus()) {
                    return;
                }
                ResultModel<VersionModel> baseModel = JsonParse.fromJson(result, new TypeToken<ResultModel<VersionModel>>() {
                });
                if (baseModel != null && baseModel.status) {
                    VersionModel model = baseModel.data;
                    if (model.isIs_delta()) {
                        LogUtil.d("html should update");
                        startDownload(model);
                    }
                    //SharedPreferencesUtils.saveObject(Hybrid.get(), UPDATE_MODEL, model);
                    Update.versionModel = model;
                    Activity activity = ActivityManager.getTagActivity();
                    if (activity != null) {
                        checkShowUpdate((AppCompatActivity) activity);
                    } else {

                    }
                }
            }
        });
    }

    public static void checkShowUpdate(AppCompatActivity activity) {
        if (isShowed) return;
//        Object ob = SharedPreferencesUtils.readObject(Hybrid.get(), UPDATE_MODEL);
        if (Update.versionModel == null) return;
        VersionModel model = Update.versionModel;
        if (model == null || TextUtils.isEmpty(model.getVersion_num())) return;

        try {
            UpdateProgressDialog dialog = new UpdateProgressDialog();

            if (model.isIs_update()) {
                //App强制升级的话，就不在处理之后的业务
                LogUtil.d("app should force update, ignore html update");
                dialog.setData(model);
                dialog.show(activity.getSupportFragmentManager(), FORCE_UPDATE);
                isShowed = true;
                return;
            }
            if (model.isIs_remaind() && (!AutoUpdateUtil.getIgnore() && !model.getVersion_num().equals(AutoUpdateUtil.getIgnoreVersion()))) {
                //App普通升级
                LogUtil.d("app should normal update");
                dialog.setData(model);
                dialog.show(activity.getSupportFragmentManager(), NO_FORCE_UPDATE);
            }
            isShowed = true;
        } catch (Throwable e) {
            isShowed = false;
        }
    }

    /**
     * 开启下载增量包的服务
     *
     * @param model
     */
    private static void startDownload(final VersionModel model) {
        LogUtil.d("delta::::start download");
        String filename = MD5.md5(model.getDelta_link()) + ".zip";
        DownLoad.INSTANT.download(model.getDelta_link(), COPY_FILE_PATH, filename, new DownloadCallback() {
            @Override
            public void requestSuccess(File file) {
                Unzip.unzip(file.getParent() + File.separator, file.getAbsolutePath());
                VersionTools.saveDeltaVersion(model.getDelta_version());
                LogUtil.d("delta::::unzip success");
            }

            @Override
            public void requestFial() {
                LogUtil.e("delta:::: zip download fail");
            }

            @Override
            public void downProgress(long readLength, long contentLength, int progress) {
                LogUtil.d("delta:::: zip download progress:" + progress + "   length:" + contentLength);
            }
        }, false);
    }
}
