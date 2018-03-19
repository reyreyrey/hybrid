package com.ivi.hybrid.utils;

import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/11/9.
 */

public enum FlurryHelper implements FlurryAgentListener {
    INSTANT;
    private boolean isOpenFlurry;
    private final String DOMAIN_REQUEST = "domain_request";
    private final String SPLASH = "splash";
    private final String ZIP = "zip";
    private final String DOMAIN_CHECK = "domain_check";

    public void initFlurry() {
        isOpenFlurry = !Hybrid.isDebug() && Config.isOpenFlurry();
        if (!isOpenFlurry) return;
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withListener(this)
                .withCaptureUncaughtExceptions(false)
                .build(Hybrid.get(), Config.getFlurryKey());
    }

    /**
     * 页面加载事件
     *
     * @param context
     */
    public void onStart(Context context) {
        if (!isOpenFlurry) return;
        FlurryAgent.onStartSession(context);
    }

    /**
     * 页面退出事件
     *
     * @param context
     */
    public void onStop(Context context) {
        if (!isOpenFlurry) return;
        FlurryAgent.onEndSession(context);
    }

    /**
     * 开始获取domain事件
     */
    private void onDomainStartRequest() {
        if (!isOpenFlurry) return;
        FlurryAgent.logEvent(DOMAIN_REQUEST, true);
    }

    /**
     * 开始检测domain事件
     */
    public void onDomainStartCheckRequest() {
        if (!isOpenFlurry) return;
        FlurryAgent.logEvent(DOMAIN_CHECK, true);
    }

    /**
     * 获取domain结束事件
     *
     * @param domain
     */
    public void onDomainEndRequest(String domain, boolean isSuccess, long time) {
        if (!isOpenFlurry) return;
        Map<String, String> map = new HashMap<>();
        map.put("domain", domain);
        map.put("time", String.valueOf(time));
        map.put("success", String.valueOf(isSuccess));
        FlurryAgent.endTimedEvent(DOMAIN_REQUEST, map);
        LogUtil.d("flurry-----onDomainEndRequest:"+time);
    }


    /**
     * 获取domain结束事件
     *
     * @param domain
     */
    public void onDomainCheckEndRequest(String domain, boolean isSuccess, long time) {
        if (!isOpenFlurry) return;
        Map<String, String> map = new HashMap<>();
        map.put("domain", domain);
        map.put("time", String.valueOf(time));
        map.put("success", String.valueOf(isSuccess));
        FlurryAgent.endTimedEvent(DOMAIN_CHECK, map);
        LogUtil.d("flurry-----onDomainCheckEndRequest:"+time);
    }

    /**
     * 启动页开始事件
     */
    private void onSplashStart() {
        if (!isOpenFlurry) return;
        FlurryAgent.logEvent(SPLASH, true);
    }

    /**
     * 启动页结束事件
     */
    public void onSplashEnd(String time) {
        if (!isOpenFlurry) return;
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        FlurryAgent.endTimedEvent(SPLASH, map);
        LogUtil.d("flurry-----onSplashEnd:"+time);
    }

    public void onZipStart() {
        if (!isOpenFlurry) return;
        FlurryAgent.logEvent(ZIP, true);
    }

    public void onZipEnd(String time) {
        if (!isOpenFlurry) return;
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        FlurryAgent.endTimedEvent(ZIP, map);
        LogUtil.d("flurry-----onZipEnd:"+time);
    }

    @Override
    public void onSessionStarted() {
        onDomainStartRequest();
        onSplashStart();
    }
}
