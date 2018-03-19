package com.ivi.hybrid.core.hotfix;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.RestrictTo;
import android.util.Log;

import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;

import java.io.File;
import java.util.List;

import javax.annotation.concurrent.Immutable;

@Immutable
@RestrictTo(RestrictTo.Scope.LIBRARY)
final class SimplePatchReporter extends DefaultPatchReporter {
    public SimplePatchReporter(Context context) {
        super(context);
    }

    @Override
    public void onPatchServiceStart(Intent intent) {
        super.onPatchServiceStart(intent);
        RealReport.onApplyPatchServiceStart();
    }

    @Override
    public void onPatchDexOptFail(File patchFile, List<File> dexFiles, Throwable t) {
        super.onPatchDexOptFail(patchFile, dexFiles, t);
        RealReport.onApplyDexOptFail(t);
    }

    @Override
    public void onPatchException(File patchFile, Throwable e) {
        super.onPatchException(patchFile, e);
        RealReport.onApplyCrash(e);
    }

    @Override
    public void onPatchInfoCorrupted(File patchFile, String oldVersion, String newVersion) {
        super.onPatchInfoCorrupted(patchFile, oldVersion, newVersion);
        RealReport.onApplyInfoCorrupted();
    }

    @Override
    public void onPatchPackageCheckFail(File patchFile, int errorCode) {
        super.onPatchPackageCheckFail(patchFile, errorCode);
        RealReport.onApplyPackageCheckFail(errorCode);
    }

    @Override
    public void onPatchResult(File patchFile, boolean success, long cost) {
        super.onPatchResult(patchFile, success, cost);
        RealReport.onApplied(cost, success);
        Log.i("tinker--------->", String.valueOf(success));
    }

    @Override
    public void onPatchTypeExtractFail(File patchFile, File extractTo, String filename, int fileType) {
        super.onPatchTypeExtractFail(patchFile, extractTo, filename, fileType);
        RealReport.onApplyExtractFail(fileType);
    }

    @Override
    public void onPatchVersionCheckFail(File patchFile, SharePatchInfo oldPatchInfo, String patchFileVersion) {
        super.onPatchVersionCheckFail(patchFile, oldPatchInfo, patchFileVersion);
        RealReport.onApplyVersionCheckFail();
    }
}
