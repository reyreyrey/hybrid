package com.ivi.hybrid.core.webview.x5.clients.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.net.download.DownLoad;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

import static com.ivi.hybrid.core.webview.x5.clients.cache.CacheUtils.CACHE_FOLDER;

/**
 * author: Rea.X
 * date: 2018/1/26.
 */

public class CacheResourceJob extends Job {
    private String url;
    private WebView webView;
    private String filename;

    protected CacheResourceJob(WebView webView, String url, String filename) {
        super(new Params(1).requireNetwork().addTags(url));
        this.webView = webView;
        this.url = url;
        this.filename = filename;
    }

    @Override
    public void onAdded() {
        LogUtil.d("CacheResourceJob:::::" + "onAdded::::" + url);
    }

    @Override
    public void onRun() throws Throwable {
        LogUtil.d("CacheResourceJob:::::" + "onRun::::" + url);
        DownLoad.INSTANT.downloadSync(url, new File(CommonUtils.getCacheDir(Hybrid.get()) + File.separator + CACHE_FOLDER, filename), webView);
        LogUtil.d("CacheResourceJob:::::" + "onRun finish::::" + url);
    }

    @Override
    protected void onCancel(int i, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int i, int i1) {
        return RetryConstraint.CANCEL;
    }
}
