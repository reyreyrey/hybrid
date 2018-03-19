package com.ivi.hybrid.core.net.download;

import android.app.PendingIntent;
import android.content.Intent;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.utils.notify.NotifyUtil;

import java.io.File;

import static com.ivi.hybrid.utils.CommonUtils.getAppName;
import static com.ivi.hybrid.utils.CommonUtils.getInstallIntent;

/**
 * author: Rea.X
 * date: 2017/11/16.
 */

public class NotifyUtils {
    public static void notifyProgress(NotifyUtil notifyUtil, int readLength, int contentLength, boolean showSound) {
        notifyUtil.notify_progress(
                null,
                Config.getAppIcon(),
                Hybrid.get().getString(R.string.hybrid_download_ticker),
                getAppName(Hybrid.get()),
                Hybrid.get().getString(R.string.hybrid_download_content),
                showSound,
                showSound,
                showSound,
                readLength,
                contentLength
        );
    }

    public static void notifySuccess(NotifyUtil notifyUtil, File file) {
        Intent intent = getInstallIntent(file);
        PendingIntent pendingIntent = PendingIntent.getActivity(Hybrid.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifyUtil.notify_normal_singline(
                pendingIntent,
                Config.getAppIcon(),
                Hybrid.get().getString(R.string.hybrid_download_ticker),
                getAppName(Hybrid.get()),
                Hybrid.get().getString(R.string.hybrid_download_success),
                true,
                true,
                true);
    }
}
