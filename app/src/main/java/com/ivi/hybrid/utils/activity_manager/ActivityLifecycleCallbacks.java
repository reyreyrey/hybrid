package com.ivi.hybrid.utils.activity_manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;


import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.utils.LogUtil;

import java.util.LinkedList;
import java.util.Set;

/**
 * author: Rea.X
 * date: 2017/3/10.
 */

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private LinkedList<Activity> activityList;
    private Set<Activity> acaActivitys;
    private boolean isAppBack;
    public Activity tagActivity;

    private LinkedList<Activity> dymanicActivitys;


    private static final String TAG = "ActivityLifecycleCallbacks->";

    public ActivityLifecycleCallbacks(LinkedList<Activity> activityList, Set<Activity> acaActivitys, LinkedList<Activity> dymanicActivitys) {
        this.activityList = activityList;
        this.acaActivitys = acaActivitys;
        this.dymanicActivitys = dymanicActivitys;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityList.add(activity);
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        tagActivity = activity;
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityResumed");
        acaActivitys.add(activity);
        if (isAppBack) {
            LogUtil.d("：：：：：：：：：：：app从后台转到前台");
            Push.getInstance().appSwitchbackOrFront(true);
            isAppBack = false;
        }
        dymanicActivitys.add(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityPaused");
        if (dymanicActivitys.contains(activity))
            dymanicActivitys.remove(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityStopped");
        if (acaActivitys.contains(activity))
            acaActivitys.remove(activity);
        if (acaActivitys.size() == 0) {
            LogUtil.d("：：：：：：：：：：：应用退出到后台");
            isAppBack = true;
            Push.getInstance().appSwitchbackOrFront(false);
            GestureCache.getInstant().appGotoBack();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d(TAG + activity.getClass().getSimpleName() + " onActivityDestroyed");
        if (activityList.contains(activity))
            activityList.remove(activity);
    }
}
