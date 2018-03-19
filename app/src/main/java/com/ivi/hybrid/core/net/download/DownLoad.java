package com.ivi.hybrid.core.net.download;

/**
 * Created by Fish.C on 2017/11/8.
 */

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.net.callback.DownloadCallback;
import com.ivi.hybrid.core.net.client.ClientManager;
import com.ivi.hybrid.core.webview.x5.tools.UserAgent;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.file.FileUtils;
import com.ivi.hybrid.utils.notify.NotifyUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;

import static com.ivi.hybrid.core.net.download.NotifyUtils.notifyProgress;
import static com.ivi.hybrid.core.net.download.NotifyUtils.notifySuccess;
import static com.ivi.hybrid.utils.CommonUtils.getNameFromUrl;

public enum DownLoad {
    INSTANT;

    private NotifyUtil notifyUtil;


    public File downloadSync(String downloadUrl, File path, WebView webView) throws IOException {
        Response response = OkGo.<File>get(downloadUrl).client(ClientManager.getDownloadClient()).headers("user-Agent", UserAgent.getUserAgent(webView)).execute();
        if (response == null || response.code() != 200) return null;
        if (path.exists())
            FileUtils.deleteFile(path);
        BufferedSink sink = Okio.buffer(Okio.sink(path));
        sink.writeAll(response.body().source());
        Util.closeQuietly(sink);
        return path;
    }

    /**
     * 下载服务，暂时不支持下载到指定位置，因为没有权限
     *
     * @param downloadUrl 下载地址
     * @param callBack    下载监听
     */
    public void download(final String downloadUrl, final DownloadCallback callBack, final boolean isShowNotify) {
        download(downloadUrl, getNameFromUrl(downloadUrl), callBack, isShowNotify);
    }

    public void download(final String downloadUrl, final String path, final String filename, final DownloadCallback callBack, final boolean isShowNotify) {
        notifyUtil = new NotifyUtil(Hybrid.get(), (int) (Math.random() * 1000));
        if (isShowNotify) {
            notifyProgress(notifyUtil, 0, 100, true);
        }
        OkGo.<File>get(downloadUrl).client(ClientManager.getDownloadClient()).execute(new FileCallback(path, filename) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                File file = response.body();
                if (callBack != null)
                    callBack.requestSuccess(file);
                if (isShowNotify)
                    notifySuccess(notifyUtil, file);
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                if (isShowNotify)
                    notifyProgress(notifyUtil, (int) (progress.fraction * 100), 100, false);
                if (callBack != null)
                    callBack.downProgress(progress.currentSize, progress.totalSize, (int) (progress.fraction * 100));
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                if (callBack != null)
                    callBack.requestFial();
            }
        });
    }

    public void download(final String downloadUrl, final String filename, final DownloadCallback callBack, final boolean isShowNotify) {
        download(downloadUrl, CommonUtils.getCacheDir(Hybrid.get()), filename, callBack, isShowNotify);
    }

}
