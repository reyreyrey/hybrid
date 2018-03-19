package com.ivi.hybrid.core.modules.forward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.config.forward.ForwardMapModel;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.activitys.HybridMainActivity;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ivi.hybrid.core.cons.Cons.CHECK_GAME_FLAG;
import static com.ivi.hybrid.core.cons.Cons.FILE_URL_BEFORE;
import static com.ivi.hybrid.core.cons.Cons.HTML_FILE_PATH;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class ForwardTools {
    private static Set<String> mainPageUrls;

    static {
        mainPageUrls = new HashSet<>();
    }

    public static void add(String url) {
        mainPageUrls.add(url);
    }

    public static void inside(PostWebView webView, ForwardModel model) {
        Activity activity = ActivityManager.getTagActivity();
        boolean newView = model.isNewView();
        boolean isForce = model.isForce();
        String url = model.getUrl();
        //判断是否是加载原生页面
        ForwardMapModel mapModel = Config.isNativalPage(url);
        if (mapModel != null) {
            toNativalPage(activity, mapModel);
            return;
        }
        String mainPageUrl = checkIsMainUrl(model);
        if (mainPageUrl != null) {
            if (activity instanceof HybridMainActivity) {
                HybridMainActivity mainActivity = (HybridMainActivity) activity;
                mainActivity.selectTab(mainPageUrl);
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("url", mainPageUrl);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
            return;
        }
        if (isForce) {
            model.setForce(true);
            toWebViewActivity(model);
            return;
        }
        if (isNewPage(activity)) {
            if (newView) {
                //如果已经是新窗口，并且newView为true,那么就在本页面打开
                HybridWebViewActivity webViewActivity = (HybridWebViewActivity) activity;
                WebViewBundleModel bundleModel = getBundleModel(model);
                webViewActivity.load(bundleModel);
            } else {
                //如果已经是新窗口，并且newView为false,那么就关闭页面，在上个页面打开
                activity.finish();
            }
        } else {
            if (newView) {
                //如果是旧窗口，并且newView为true,那么就跳转到新页面
                toWebViewActivity(model);
            } else {
                //如果是旧窗口，并且newView为false,那么就在本页面打开
                WebViewBundleModel bundleModel = getBundleModel(model);
                webView.loadUrl(bundleModel.url);
            }
        }

    }

    /**
     * 跳转到原生页面
     *
     * @param activity
     * @param mapModel
     */
    private static void toNativalPage(Activity activity, ForwardMapModel mapModel) {
        Intent intent = new Intent(activity, mapModel.getActivity());
        if (mapModel.getFlag() != -1)
            intent.addFlags(mapModel.getFlag());
        if (mapModel.getBundle() != null)
            intent.putExtras(mapModel.getBundle());
        if (mapModel.getRequestCode() != -1)
            activity.startActivityForResult(intent, mapModel.getRequestCode());
        else
            activity.startActivity(intent);

    }


    public static void outside(PostWebView webView, ForwardModel model) {
        Activity activity = ActivityManager.getTagActivity();
        String url = model.getUrl();
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(model.getGameType()))
            url = uri.buildUpon().appendQueryParameter(CHECK_GAME_FLAG, model.getGameType()).build().toString();
        model.setUrl(url);
        boolean newView = model.isNewView();
        boolean browser = model.isBrowser();
        boolean isGame = !TextUtils.isEmpty(model.getGameType());
        //是否在新页面打开
        boolean isForce = model.isForce();
        if (browser) {
            openBrower(activity, url);
            return;
        }
        if (isForce) {
            model.setForce(true);
            toWebViewActivity(model);
            return;
        }
        if (isNewPage(activity)) {
            if (newView) {
                //如果已经是新窗口，并且newView为true,那么就在本页面打开
                HybridWebViewActivity webViewActivity = (HybridWebViewActivity) activity;
                WebViewBundleModel bundleModel = getBundleModel(model);
                webViewActivity.load(bundleModel);
            } else {
                //如果已经是新窗口，并且newView为false,那么就关闭页面，在上个页面打开
                activity.finish();
            }
        } else {
            if (newView) {
                //如果是旧窗口，并且newView为true,那么就跳转到新页面
                toWebViewActivity(model);
            } else {
                //如果是旧窗口，并且newView为false,那么就在本页面打开
                HybridWebViewActivity webViewActivity = (HybridWebViewActivity) activity;
                WebViewBundleModel bundleModel = getBundleModel(model);
                webViewActivity.load(bundleModel);
            }
        }
    }


    /**
     * 检测是否是首页的4个链接
     *
     * @param m 需要检测的model
     * @return 首页链接
     */
    @Nullable
    private static String checkIsMainUrl(ForwardModel m) {
        if (m.isNewView()) return null;
        String url = m.getUrl();
        if (TextUtils.isEmpty(url)) return null;
        int position = 0;
        for (String originUrl : mainPageUrls) {
            if (TextUtils.isEmpty(originUrl)) continue;
            String tagUrl = originUrl;
            if (url.startsWith(FILE_URL_BEFORE)) {
                tagUrl = FILE_URL_BEFORE + HTML_FILE_PATH + originUrl;
            }
            if (url.contains("?")) {
                int index = url.indexOf("?");
                url = url.substring(0, index);
            }
            if (url.contains("#")) {
                int index = url.indexOf("#");
                url = url.substring(0, index);
            }
            if (url.equals(tagUrl)) {
                return originUrl;
            }
            position++;
        }
        return null;
    }

    private static void toWebViewActivity(ForwardModel model) {
        WebViewBundleModel bundleModel = getBundleModel(model);
        HybridWebViewActivity.openWebView(bundleModel, false);
    }

    private static WebViewBundleModel getBundleModel(ForwardModel model) {
        String originUrl = model.getUrl();
        if (!originUrl.startsWith("http://") && !originUrl.startsWith("https://") && !originUrl.startsWith("file:///")) {
            originUrl = FILE_URL_BEFORE + HTML_FILE_PATH + originUrl;
        }
        return new WebViewBundleModel(originUrl, !TextUtils.isEmpty(model.getGameType()), model.getTheme(), model.isFullscreen(), model.getMenu(), null, model.isForce());
    }

    private static boolean isNewPage(Activity activity) {
        return activity instanceof HybridWebViewActivity;
    }

    private static void openBrower(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ignored) {
            System.out.println("");
        }
    }

}
