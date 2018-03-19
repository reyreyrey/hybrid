package com.ivi.hybrid.utils.activity_manager;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;


import com.ivi.hybrid.utils.LogUtil;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * author: Rea.X
 * date: 2017/3/27.
 * <p>Activity堆栈监听类</p>
 * <p>实现了应用进入后台和应用进入前台的监听</p>
 * <p>待解决bug:</p>
 * <p>1.当应用中有多进程的时候，会出现一些误判</p>
 * <p>当程序中存在多进程的时候，建议使用CommonUtils.isAppInTheForeground来判断程序是否处于前台</p>
 * <p>需要在Application中调用startWatcher</p>
 */

public class ActivityManager {
    private static LinkedList<Activity> activityList;
    private static Set<Activity> acaActivitys;
    private static ActivityLifecycleCallbacks callbacks;
    private static Application app;
    private static LinkedList<Activity> dymanicActivitys;

    /**
     * 需在Application中注册
     *
     * @param application
     */
    public static void startWatcher(Application application) {
        if (callbacks != null || activityList != null || acaActivitys != null) return;
        activityList = new LinkedList<>();
        acaActivitys = new HashSet<>();
        dymanicActivitys = new LinkedList<>();
        app = application;
        callbacks = new ActivityLifecycleCallbacks(activityList, acaActivitys, dymanicActivitys);
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public static void stopWatcher() {
        if (app == null || callbacks == null) return;
        app.unregisterActivityLifecycleCallbacks(callbacks);
    }

    /**
     * App是否在前台
     *
     * @return
     */
    public static boolean appIsFont() {
        if (acaActivitys == null)
            throw new RuntimeException("you should invoke startWatcher method frist");
        return acaActivitys.size() != 0;
    }

    /**
     * 退出程序
     */
    public static void exitApp() {
        if (activityList == null)
            throw new RuntimeException("you should invoke startWatcher method frist");
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public static void toActivity(Class<? extends Activity> cls) {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            if (activity.getClass().equals(cls)) return;
            activity.finish();
        }
    }

    public static Activity getTagActivity() {
        if (callbacks != null)
            return callbacks.tagActivity;
        return null;
    }

    public static List<Activity> getStackActivitys() {
        return activityList;
    }

    public static List<Activity> getDymanicActivitys() {
        return dymanicActivitys;
    }
}