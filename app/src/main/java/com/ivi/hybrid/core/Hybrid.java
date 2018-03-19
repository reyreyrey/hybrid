package com.ivi.hybrid.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.facebook.stetho.Stetho;
import com.ivi.hybrid.core.config.HybridConfig;
import com.ivi.hybrid.core.daemon.Daemon;
import com.ivi.hybrid.core.exception.BridgeException;
import com.ivi.hybrid.core.exception.CrashHandler;
import com.ivi.hybrid.core.hotfix.Patcher;
import com.ivi.hybrid.core.log.Log;
import com.ivi.hybrid.core.modules.driver.ChannelModel;
import com.ivi.hybrid.core.net.client.ClientManager;
import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.core.update.Update;
import com.ivi.hybrid.gesture.config.GestureConfig;
import com.ivi.hybrid.utils.AgentTools;
import com.ivi.hybrid.utils.ClipboardTools;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.FlurryHelper;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.lzy.okgo.OkGo;
import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import cn.onlinecache.breakpad.NativeBreakpad;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;
import okio.BufferedSource;
import okio.Okio;

import static com.ivi.hybrid.core.cons.Cons.NATIVAL_CARSH_DIR;

/**
 * author: Rea.X
 * date: 2017/10/25.
 */

public class Hybrid {
    public static final int LOCAL_MODEL = 1;
    public static final int RUNTIME_TEST_MODEL = 2;
    public static final int RUNTIME_MODEL = 3;
    private static final int METHOD_COUNT = 5;
    private static final int METHOD_OFFSET = 0;
    private static final String LOG_TAG = "logger tag:";
    private static Application application;
    private static GestureConfig gestureConfig;
    private static int model;
    private static boolean isDebug;
    private static String sessionID;
    private static HybridConfig config;
    private static JobManager jobManager;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static Application get() {
        return application;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static String getChannelID() {
        return AgentTools.getChannelID(application);
    }

    public static JobManager getJobManager() {
        return jobManager;
    }

    public static
    @RunModel
    int getRunModel() {
        return model;
    }

    public static void gesture(GestureConfig gestureConfig) {
        Hybrid.gestureConfig = gestureConfig;
    }

    @SuppressLint("CheckResult")
    public static void init(Application application, @RunModel int model, boolean isDebug, HybridConfig config) {
        Hybrid.application = application;
        Hybrid.model = model;
        Hybrid.isDebug = isDebug;
        Hybrid.config = config;
        LitePal.initialize(application);
        if (CommonUtils.isServicePid(application)) return;
        ActivityManager.startWatcher(application);
        Push.getInstance().start(application);
        initX5();
        FlurryHelper.INSTANT.initFlurry();

        Log.init(application);
        if (!isDebug) {
            CrashHandler catchHandler = CrashHandler.getInstance();
            catchHandler.init(application, model, isDebug, config);
            File file = new File(NATIVAL_CARSH_DIR);
            if (!file.exists()) file.mkdirs();
            NativeBreakpad.init(NATIVAL_CARSH_DIR);
        }
        initDaemon(application);
        OkGo.getInstance().setOkHttpClient(ClientManager.getDefaultHttpClient()).init(application);
        if (model != RUNTIME_MODEL) {
            //chrome查看本地数据库及sp,页面调试
            Stetho.initializeWithDefaults(application);
        }
//        if (Config.isOpenGesture())
//            Helper.init(application, new IGestureListener(application), new IGestureSetting(application, null, 0), null);
        Patcher.apply();
        configureJobManager();

        ClipboardTools.init(application);
        Update.startCheck();

        /*agent(application)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        ClipboardTools.init(application);
                        Update.startCheck();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Update.startCheck();
                    }
                });*/

    }

    public static GestureConfig getGestureConfig() {
        if (gestureConfig == null) {
            throw new BridgeException("gestureConfig must not null");
        }
        return gestureConfig;
    }

    private static void initDaemon(final Application application) {
        com.evernote.android.job.JobManager
                .create(application)
                .addJobCreator(new JobCreator() {
                    @Override
                    public Job create(@NonNull String s) {
                        switch (s) {
                            case Daemon.TAG:
                                return new Daemon(application);
                            default:
                                throw new UnsupportedOperationException();
                        }

                    }
                });
    }

    private static void initX5() {
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(application, new QbSdk.PreInitCallback() {
            public void onCoreInitFinished() {
                LogUtil.d("onCoreInitFinished x5 core load success--------");
            }

            public void onViewInitFinished(boolean b) {
                LogUtil.d("onViewInitFinished b--------" + b);
            }
        });
    }


    public static String getSessionID() {
        if (sessionID == null)
            sessionID = UUID.randomUUID().toString().replaceAll("-", "");
        return sessionID;
    }

    public static HybridConfig getConfig() {
        return config;
    }

    private static void configureJobManager() {
        //3. JobManager的配置器，利用Builder模式
        Configuration configuration = new Configuration.Builder(application)
                .customLogger(new CustomLogger() {
                    @Override
                    public boolean isDebugEnabled() {
                        return isDebug;
                    }

                    @Override
                    public void d(String s, Object... objects) {
                        android.util.Log.d(LOG_TAG, String.format(s, objects));
                    }

                    @Override
                    public void e(Throwable throwable, String s, Object... objects) {
                        android.util.Log.e(LOG_TAG, String.format(s, objects));
                    }

                    @Override
                    public void e(String s, Object... objects) {
                        android.util.Log.e(LOG_TAG, String.format(s, objects));
                    }

                    @Override
                    public void v(String s, Object... objects) {
                        android.util.Log.v(LOG_TAG, String.format(s, objects));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(configuration);
    }
}

