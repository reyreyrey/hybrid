package com.ivi.hybrid.core.webview.x5.clients;

import android.content.Context;

import com.ivi.hybrid.core.update.DownloadAppService;
import com.ivi.hybrid.utils.LogUtil;
import com.tencent.smtt.sdk.DownloadListener;

/**
 * author: Rea.X
 * date: 2017/10/13.
 */

public class IDownloadListener implements DownloadListener {
    private Context context;

    public IDownloadListener(Context context) {
        this.context = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long length) {
        LogUtil.d("onDownloadStart:::::url:" + url);
        LogUtil.d("onDownloadStart:::::userAgent:" + userAgent);
        LogUtil.d("onDownloadStart:::::contentDisposition:" + contentDisposition);
        LogUtil.d("onDownloadStart:::::mimeType:" + mimeType);
        LogUtil.d("onDownloadStart:::::length:" + length);
        DownloadAppService.downloadAppAndInstall(context, url, true);

    }


}
