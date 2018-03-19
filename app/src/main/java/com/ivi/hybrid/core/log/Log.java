package com.ivi.hybrid.core.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.ReadFileUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

import static com.ivi.hybrid.core.cons.Cons.ACTION_LOG_SERVICE;
import static com.ivi.hybrid.core.cons.Cons.NATIVAL_CARSH_DIR;
import static com.ivi.hybrid.core.log.LogMessage.UN_CAUGHT_EXCEPTION;
import static com.ivi.hybrid.core.push.utils.Utils.logServiceIsWork;

/**
 * author: Rea.X
 * date: 2017/11/14.
 */

public class Log {
    private static volatile boolean isUploading = false;

    public static void init(Context context) {
        if (logServiceIsWork(context)) return;
        startLogService(context);
    }

    private static void startLogService(Context context) {
        Intent intent = new Intent(context, LogService.class);
        intent.setAction(ACTION_LOG_SERVICE);
        intent.setPackage(context.getPackageName());
        ContextCompat.startForegroundService(context, intent);
    }

    @SuppressLint("CheckResult")
    static void upload(Context context) {
        LogUtil.d("start upload");
        if (!CommonUtils.checkNet(context)) return;
        if (isUploading) return;
        isUploading = true;
        Observable
                .just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {

                    @Override
                    public String apply(String s) throws Exception {
                        LogUtil.d("upload start");
                        uploadSync();
                        uploadStartupLog();
                        LogUtil.d("upload finish");
                        return "1";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        isUploading = false;
                    }
                });
    }

    public static boolean isUploading() {
        return isUploading;
    }

    private static void uploadStartupLog() {
        try {
            List<StartupMessage> messages = DataSupport.findAll(StartupMessage.class);
            if (messages != null && messages.size() != 0) {
                for (StartupMessage logMessage : messages) {
                    Response response = HybridRequest.request(logMessage.getParams(), "app/appStartUpCollect");
                    String result = response.body().string();
                    InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                    if (interceptModel != null && interceptModel.status) {
                        logMessage.delete();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void uploadSync() {
        try {
            List<LogMessage> messages = DataSupport.findAll(LogMessage.class);
            try {
//                ReadFileUtils.find(NATIVAL_CARSH_DIR, messages);
            } catch (Exception e) {
            }
            if (messages != null && messages.size() != 0) {
                for (LogMessage logMessage : messages) {
                    Response response = HybridRequest.request(logMessage.getParams(), "app/flurrylogCollect");
                    String result = response.body().string();
                    InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                    if (interceptModel != null && interceptModel.status) {
                        if (logMessage.getFileAbsolutePath() != null) {
                            File file = new File(logMessage.getFileAbsolutePath());
                            try {
                                file.delete();
                            } catch (Exception e) {
                            }
                        }
                        logMessage.delete();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
