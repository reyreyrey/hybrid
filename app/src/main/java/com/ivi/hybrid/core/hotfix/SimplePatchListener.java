package com.ivi.hybrid.core.hotfix;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RestrictTo;

import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;
import java.util.Properties;

import javax.annotation.concurrent.Immutable;

@Immutable
@RestrictTo(RestrictTo.Scope.LIBRARY)
final class SimplePatchListener extends DefaultPatchListener {
    private static final String TAG = "Tinker.SimplePatchListener";

    protected static final long NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = 60 * 1024 * 1024;

    private final int maxMemory;

    public SimplePatchListener(Context context) {
        super(context);
        maxMemory = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        TinkerLog.i(TAG, "Max memory:" + maxMemory);
    }

    public int patchCheck(String path, String patchMd5) {
        File patchFile = new File(path);
        TinkerLog.i(TAG,
                "receive a patch file: %s, file size:%d",
                path,
                SharePatchFileUtil.getFileOrDirectorySize(patchFile));
        int returnCode = super.patchCheck(path, patchMd5);

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            returnCode = TinkerUtil.checkForPatchRecover(NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN, maxMemory);
        }

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            SharedPreferences sp = context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG,
                    Context.MODE_MULTI_PROCESS);
            //optional, only disable this patch file with md5
            int fastCrashCount = sp.getInt(patchMd5, 0);
            if (fastCrashCount >= UncaughtExceptionHandler.MAX_CRASH_COUNT) {
                returnCode = TinkerUtil.ERROR_PATCH_CRASH_LIMIT;
            }
        }
        // Warning, it is just a sample case, you don't need to copy all of these
        // Interception some of the request
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            Properties properties = ShareTinkerInternals.fastGetPatchPackageMeta(patchFile);
            if (properties == null) {
                returnCode = TinkerUtil.ERROR_PATCH_CONDITION_NOT_SATISFIED;
            } else {
                String platform = properties.getProperty(TinkerUtil.PLATFORM);
                TinkerLog.i(TAG, "get platform:" + platform);
                // check patch platform require
                if (platform == null || !platform.equals(MockBuild.PLATFORM)) {
                    returnCode = TinkerUtil.ERROR_PATCH_CONDITION_NOT_SATISFIED;
                }
            }
        }
        RealReport.onTryApply(returnCode == ShareConstants.ERROR_PATCH_OK);
        return returnCode;
    }
}
