package com.ivi.hybrid.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.BarHide;
import com.hybrid.R;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.config.menu.MenuModel;
import com.ivi.hybrid.core.config.theme.Themes;
import com.ivi.hybrid.core.exception.BridgeException;
import com.ivi.hybrid.core.modules.forward.WebViewBundleModel;
import com.ivi.hybrid.core.net.client.ClientManager;
import com.ivi.hybrid.core.update.Update;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.core.webview.x5.ProgressWebView;
import com.ivi.hybrid.core.webview.x5.dns.PostInterceptJavascriptInterface;
import com.ivi.hybrid.core.webview.x5.tools.IOrientoinListener;
import com.ivi.hybrid.core.webview.x5.tools.WebTools;
import com.ivi.hybrid.gesture.GestureManager;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.ui.clients.LoadFinishWebClient;
import com.ivi.hybrid.utils.BackConfigHelper;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.ToastUtils;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.lzy.okgo.OkGo;
import com.mylhyl.circledialog.CircleDialog2;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;

import static com.ivi.hybrid.core.cons.Cons.TO_NEW_WEB;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class HybridWebViewActivity extends UIActivity implements View.OnClickListener {
    //外部传入的参数
    private WebViewBundleModel bundleModel;
    protected ProgressWebView progressView;
    protected PostWebView webview;
    protected Toolbar toolbar;
    protected IOrientoinListener listener;
    protected TextView tvTitle;
    protected TextView tvBack;
    protected String backUrl;
    protected long exitTime;
    protected boolean hasOrientationEventListener = false;

    public static void openWebView(WebViewBundleModel model, boolean isNewTask) {
        Activity activity = ActivityManager.getTagActivity();
        if (activity == null) return;
        if (activity instanceof HybridWebViewActivity && !model.isForce && !Config.isFroceOpenNewview()) {
            HybridWebViewActivity hybridWebViewActivity = (HybridWebViewActivity) activity;
            hybridWebViewActivity.load(model);
            return;
        }
        Class<? extends HybridWebViewActivity> cls = Config.getCustomerWebViewActivity();
        if (cls == null)
            throw new BridgeException("you should Override HybridConfig method customerWebViewActivity");
        Intent intent = new Intent(activity, cls);
        intent.putExtra("data", model);
        if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, TO_NEW_WEB);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.hybrid_activity_webview;
    }

    @Override
    protected void init() {
        progressView = (ProgressWebView) findViewById(R.id.progress_webview);
        webview = progressView.getWebView();
        webview.addJavascriptInterface(new PostInterceptJavascriptInterface(webview), "appClient");
        initScreen();
        initToolbar();
        progressView.setIWebViewClient(new HybridClient());
        bundleModel = (WebViewBundleModel) getIntent().getSerializableExtra("data");
        load(bundleModel);
    }

    private void initToolbar() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(null);
        tvBack.setOnClickListener(this);
        setSupportActionBar(toolbar);
        CommonUtils.onClickNumber(this, toolbar);
    }

    private void refreshTheme() {
        try {
            Themes themes = Config.getTheme(bundleModel.theme);
            tvTitle.setTextColor(themes.getTitleTextColor());
            toolbar.setBackground(themes.getToolbarBackground());
            tvBack.setCompoundDrawablesWithIntrinsicBounds(themes.getBackIcon(), 0, 0, 0);
            immersionBar
                    .navigationBarColorInt(themes.getNavigationBarColor());
            statusbarView.setBackground(themes.getStatusBarbackground());
        } catch (Throwable e) {
        }
    }

    public void load(WebViewBundleModel bundleModel) {
        if (null == bundleModel) {
            onBackPressed();
            return;
        }
        this.bundleModel = bundleModel;
        refreshTheme();
        ArrayList<String> cookies = bundleModel.cookies;
        if (cookies != null && cookies.size() > 0) {
            for (String cookie : cookies) {
                CookieManager.getInstance().setCookie(bundleModel.url, cookie);
                CookieManager.getInstance().flush();
            }
        }
        refreshTheme(bundleModel);
        webview.loadUrl(bundleModel.url);
        supportInvalidateOptionsMenu();
    }

    protected void refreshTheme(WebViewBundleModel bundleModel) {
        if (bundleModel.fullScreen || bundleModel.isGame) {
            statusbarView.setVisibility(View.GONE);
            immersionBar.hideBar(BarHide.FLAG_HIDE_BAR).init();
        } else {
            statusbarView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            immersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
        }
        if (bundleModel.isGame && !hasOrientationEventListener) {
            if (listener == null) {
                listener = new IOrientoinListener(this);
            }
            hasOrientationEventListener = true;
            listener.enable();
        }
        toolbar.setVisibility(bundleModel.enable_exit ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        refreshTheme(bundleModel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OkGo.cancelTag(ClientManager.getDefaultHttpClient(), getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebTools.releaseWebView(webview);
        OkGo.cancelTag(ClientManager.getDefaultHttpClient(), getClass().getName());
        if (hasOrientationEventListener) {
            listener.disable();
            hasOrientationEventListener = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
    }

    public PostWebView getWebView() {
        return webview;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
        if (GestureCache.getInstant().isAppFormBackToForward())
            GestureManager.showGestureLock(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_back) {
            checkFinish();
        }
    }

    private void checkFinish() {
        if (!bundleModel.enable_exit) {
            if (exitTime == 0 || (System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                ToastUtils.toastWarn(context, getString(R.string.hybrid_exit_app));
                return;
            }
            ActivityManager.exitApp();
            return;
        }
        if (TextUtils.isEmpty(backUrl)) {
            if (bundleModel.isGame) {
                showDialog();
                return;
            }
            finish();
            return;
        }

        if (backUrl.startsWith("javascript:")) {
            webview.evaluateJavascript(backUrl, null);
            return;
        }
        webview.loadUrl(backUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toolbar.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolbar.setVisibility(View.VISIBLE);

        }
    }

    private void showDialog() {
        new CircleDialog2.Builder(context)
                .setCanceledOnTouchOutside(false)
                .setCancelable(true)
                .setTitle(getString(R.string.hybrid_alert_tip))
                .setText(getString(R.string.hybrid_exit_game))
                .setNegative(getString(R.string.hybrid_common_exit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setPositive(getString(R.string.hybrid_common_cancel), null)
                .show();
    }


    private class HybridClient extends LoadFinishWebClient {

        HybridClient() {
            super(progressView, tvTitle);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            backUrl = BackConfigHelper.getBackUrl(url);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        refreshMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        refreshMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void refreshMenu(Menu menu) {
        if (bundleModel == null) return;
        MenuModel menuModel = Config.getMenu(bundleModel.menu);
        if (menuModel == null) return;
        for (MenuModel.MenuItemModel model : menuModel.getModels()) {
            getMenuInflater().inflate(model.getMenuId(), menu);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressView.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
