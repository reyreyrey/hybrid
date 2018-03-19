package com.ivi.hybrid.core.exception;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.RunModel;
import com.ivi.hybrid.core.config.HybridConfig;
import com.ivi.hybrid.core.log.LogMessage;
import com.ivi.hybrid.gesture.config.GestureConfig;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import org.litepal.crud.DataSupport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.ivi.hybrid.core.log.LogMessage.UN_CAUGHT_EXCEPTION;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private Application application;
    private int model;
    private boolean isDebug;
    private HybridConfig config;
    private GestureConfig gestureConfig;

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    private LogMessage logMessage;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }


    /**
     * 初始化
     */
    public void init(Application application, @RunModel int model, boolean isDebug, HybridConfig config) {
        this.application = application;
        this.model = model;
        this.isDebug = isDebug;
        this.config = config;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.logMessage = new LogMessage();
        this.logMessage.setType(UN_CAUGHT_EXCEPTION);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // 退出程序
            Process.killProcess(Process.myPid());
            ActivityManager.exitApp();
        }
    }

    long temptime = 0;

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Hybrid.init(application, model, isDebug, config);
                reStart();
                Looper.loop();
            }
        }.start();
        try {
            String result = getErrorMessage(ex);
            this.logMessage.setMessage(result);
            temptime = System.currentTimeMillis();
            LogUtil.d("logMessage" + this.logMessage.toString());
            int count = DataSupport.where("message=?", result).count(LogMessage.class);
            if (count == 0) {
                this.logMessage.save();
            }
        } catch (Throwable e) {
        }
        return true;
    }

    private void reStart() {
        Activity activity = ActivityManager.getTagActivity();
        if (activity == null) {
            Intent intent = new Intent(application, CommonUtils.getLaunchActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, CommonUtils.getLaunchActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String getErrorMessage(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        LogUtil.e(result);
        return result;
    }
}
