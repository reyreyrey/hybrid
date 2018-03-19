package com.ivi.hybrid.core.hotfix;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.multidex.MultiDex;

import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

public abstract class TinkerAppLike extends DefaultApplicationLike {
    private static final String TAG = "Tinker.TinkerAppLike";
    private static boolean isInstalled = false;
    private static DefaultApplicationLike instance;

    public static Application APP;
    public static Context CONTEXT;

    public static DefaultApplicationLike getInstance() {
        return instance;
    }

    public TinkerAppLike(Application application,
                         int tinkerFlags,
                         boolean tinkerLoadVerifyFlag,
                         long applicationStartElapsedTime,
                         long applicationStartMillisTime,
                         Intent tinkerResultIntent) {
        super(application,
                tinkerFlags,
                tinkerLoadVerifyFlag,
                applicationStartElapsedTime,
                applicationStartMillisTime,
                tinkerResultIntent);
        if (instance == null) {
            instance = this;
        }
    }

    @CallSuper
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //Note: must install multiDex at first
        MultiDex.install(base);
        APP = getApplication();
        CONTEXT = getApplication();

        //should set before tinker is installed
        UpgradePatchRetry.getInstance(getApplication()).setRetryEnable(true);
        //optional set logIml, or you can use default debug log
        SimpleLog simpleLog = new SimpleLog();
        simpleLog.setLevel(SimpleLog.LEVEL_ERROR);
        TinkerInstaller.setLogIml(simpleLog);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());

        //installTinker after load multiDex
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(this,
                new SimpleLoadReporter(APP),
                new SimplePatchReporter(APP),
                new SimplePatchListener(APP),
                ResultService.class,
                new UpgradePatch());
        isInstalled = true;
        Tinker.with(APP);
    }

}
