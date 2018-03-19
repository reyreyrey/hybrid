package com.ivi.hybrid.ui.activitys;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.hybrid.R;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.daemon.Daemon;
import com.ivi.hybrid.core.modules.forward.WebViewBundleModel;
import com.ivi.hybrid.core.update.Update;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.gesture.GestureManager;
import com.ivi.hybrid.gesture.listeners.GestureListener;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.ui.views.NoScrollViewPager;
import com.ivi.hybrid.ui.views.splash.SplashView;
import com.ivi.hybrid.utils.AreaLimt;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.ToastUtils;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.ivi.hybrid.utils.collectDevice.CollectDeviceInfoTools;

import static com.ivi.hybrid.core.cons.Cons.TO_NEW_WEB;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public abstract class HybridMainActivity extends UIActivity implements SplashView.OnSplashListener, RadioGroup.OnCheckedChangeListener, GestureListener {

    protected NoScrollViewPager viewPager;
    protected RadioGroup radioGroup;
    protected SplashView splashView;
    private String pushMessageLink;
    private RelativeLayout mainRoot;
    private View mainLine;

    @Override
    protected final int getLayoutId() {
        return R.layout.hybrid_activity_main;
    }

    @Override
    protected final void init() {
        initView();
        Daemon.schedule();
        CollectDeviceInfoTools.collect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Update.checkShowUpdate(this);
        if (!splashView.isShowing()) {
            if (GestureCache.getInstant().isAppFormBackToForward())
                GestureManager.showGestureLock(this, this);
        }
    }


    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(needPreloadCount());
        splashView = (SplashView) findViewById(R.id.splash_view);
        splashView.setOnSplashListener(this);
        splashView.setPreloadCount(needPreloadCount());
        splashView.startRequest();
        radioGroup.setOnCheckedChangeListener(this);
        mainRoot = findViewById(R.id.main_root);
        mainLine = findViewById(R.id.main_line);
    }

    public void preloadFinish(boolean isFirstPage) {
        splashView.preloadSuccess(isFirstPage);
    }

    @Override
    public void onSplashDismiss() {
        initScreen();
        if (GestureManager.isOpenGesture()) {
            GestureManager.showGestureLock(this, this);
            return;
        }
        initAfterSplashDismiss();
    }

    private void initAfterSplashDismiss() {
        receiverPushMessage();
        AreaLimt.checkAreaLimit();
        Update.checkShowUpdate(this);
    }

    @Override
    public void onCopyExtraFinished() {
        needInit();
    }

    /**
     * 开始初始化
     */
    protected abstract void needInit();

    /**
     * 获取需要预加载的个数
     *
     * @return 获取需要预加载的个数
     */
    protected abstract int needPreloadCount();

    public abstract PostWebView getCurrentWebView();

    /**
     * 当前加载的url是主页的url，需要切换标签
     *
     * @param url
     */
    public abstract void selectTab(String url);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_NEW_WEB:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String url = data.getStringExtra("url");
                        selectTab(url);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void receiverPushMessage() {
        Intent intent = getIntent();
        String pushMessageid = intent.getStringExtra("id");
        String pushMessageLink = intent.getStringExtra("link");
        if (TextUtils.isEmpty(pushMessageid) && TextUtils.isEmpty(pushMessageLink)) return;
        if (!TextUtils.isEmpty(pushMessageLink)) {
            HybridWebViewActivity.openWebView(new WebViewBundleModel(pushMessageLink, false, -1, false, -1), false);
            return;
        }
        if (pushMessageid.equalsIgnoreCase("-1")) return;
        CallModule.callInside(Config.getLetterpageDetailAddress(pushMessageid));
    }

    @Override
    public void onGestureInputFinish(DialogFragment dialogFragment, String pwd, boolean isRight) {
        if (isRight) {
            initAfterSplashDismiss();
        }
    }

    private long backTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (splashView.getVisibility() == View.VISIBLE) return true;
            if (backTime == 0) {
                backTime = System.currentTimeMillis();
                ToastUtils.toastWarn(this, getString(R.string.hybrid_exit_app));
                return true;
            }
            if ((System.currentTimeMillis() - backTime) >= 2000) {
                backTime = System.currentTimeMillis();
                ToastUtils.toastWarn(this, getString(R.string.hybrid_exit_app));
                return true;
            }
            ActivityManager.exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public RelativeLayout getMainRoot() {
        return mainRoot;
    }

    public View getMainLine() {
        return mainLine;
    }
}
