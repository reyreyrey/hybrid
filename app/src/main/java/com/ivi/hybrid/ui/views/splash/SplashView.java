package com.ivi.hybrid.ui.views.splash;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.log.StartupMessage;
import com.ivi.hybrid.utils.FlurryHelper;
import com.ivi.hybrid.utils.LogUtil;

import io.reactivex.functions.Consumer;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class SplashView extends LinearLayout implements SplashViewUtils.OnDomaingetCallback {
    private AppCompatActivity activity;
    private boolean isZipSuccess, isDomainGetSuccess, isPreloadSuccess;
    private volatile int preloadSuccessCount = 0;
    private ImmersionBar immersionBar;
    private long lanuchTime;
    private boolean isDismissed = false;
    public static final String TIME_TAG = "timestmap:::";
    private int preLoadCount;

    public SplashView(Context context) {
        super(context);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        lanuchTime = System.currentTimeMillis();
        activity = (AppCompatActivity) context;
        immersionBar = ImmersionBar.with(activity).hideBar(BarHide.FLAG_HIDE_BAR);
        immersionBar.init();
        LayoutInflater.from(context).inflate(Config.getSplashLayout(), this, true);

    }

    public void startRequest() {
        //请求domain
        SplashViewUtils.checkDomain(this);
        //解压
        unzip();
    }

    public boolean isShowing() {
        return !isDismissed;
    }

    /**
     * 复制解压
     */
    private void unzip() {
        final long copyTime = System.currentTimeMillis();
        SplashViewUtils.unzip()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String filePath) throws Exception {
                        if (listener != null)
                            listener.onCopyExtraFinished();
                        isZipSuccess = true;
                        LogUtil.d(TIME_TAG + "复制解压耗时：" + (System.currentTimeMillis() - copyTime));
                        checkDismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private synchronized void checkDismiss() {
        if (isDomainGetSuccess && isZipSuccess && isPreloadSuccess && !isDismissed) {
            isDismissed = true;
            setVisibility(View.GONE);
            immersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
            long splashTime = (System.currentTimeMillis() - lanuchTime);
            LogUtil.d(TIME_TAG + "启动耗时：" + splashTime);
            if (listener != null)
                listener.onSplashDismiss();
            FlurryHelper.INSTANT.onSplashEnd(String.valueOf(splashTime));
            //保存启动信息
            new StartupMessage().save();
        }
    }


    @Override
    public void onGetSuccess() {
        isDomainGetSuccess = true;
        checkDismiss();
    }

    /**
     * 设置预加载数量
     *
     * @param count 预加载数量
     */
    public void setPreloadCount(int count) {
        this.preLoadCount = count;
        if (this.preLoadCount <= 0) {
            isPreloadSuccess = true;
            checkDismiss();
        }
    }

    public synchronized void preloadSuccess(boolean isFirstPage) {
//        if(isFirstPage){
//            isPreloadSuccess = true;
//            checkDismiss();
//        }
        preloadSuccessCount++;
        if (preloadSuccessCount >= preLoadCount) {
            isPreloadSuccess = true;
            checkDismiss();
        }
    }

    private OnSplashListener listener;

    public void setOnSplashListener(OnSplashListener listener) {
        this.listener = listener;
    }

    public interface OnSplashListener {
        void onSplashDismiss();

        void onCopyExtraFinished();
    }

}
