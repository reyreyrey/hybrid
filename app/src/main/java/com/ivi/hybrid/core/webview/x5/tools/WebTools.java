package com.ivi.hybrid.core.webview.x5.tools;

import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.net.tools.AppTokenHelper;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.ivi.hybrid.core.cons.Cons.CHECK_GAME_FLAG;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class WebTools {

    private static final String KEY_USER_TOKEN = "userToken";
    private static final String KEY_APP_TOKEN = "appToken";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_ACCOUNT_TOKEN = "accountToken";

    /**
     * 删除url中的用户信息
     *
     * @param url
     * @return
     */
    public static String urlDeleteUserMsg(String url) {
        if (TextUtils.isEmpty(url)) return null;
        Uri originUri = Uri.parse(url);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(originUri.getScheme());
        builder.authority(originUri.getAuthority());
        builder.path(originUri.getPath());
        builder.fragment(originUri.getFragment());
        Set<String> queryParams = originUri.getQueryParameterNames();
        for (String key : queryParams) {
            if (key.equals(KEY_USER_TOKEN) || key.equals(KEY_APP_TOKEN) || key.equals(KEY_ACCOUNT_NAME) || key.equals(KEY_ACCOUNT_NAME)) {
                continue;
            }
            builder.appendQueryParameter(key, originUri.getQueryParameter(key));
        }
        return builder.build().toString();
    }


    public static Map<String, String> getUserParams() {
        Map<String, String> params = new HashMap<>();
        UserModel userModel = UserHelper.INSTANT.getUserModel();
        String appToken = AppTokenHelper.INSTANT.getAppToken();
        boolean isLogin = false;
        if (userModel == null || TextUtils.isEmpty(userModel.getLogin_name()) || TextUtils.isEmpty(userModel.getUser_token())) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        if (isLogin) {
            params.put(KEY_ACCOUNT_NAME, userModel.getLogin_name());
            params.put(KEY_APP_TOKEN, appToken);
            params.put(KEY_USER_TOKEN, userModel.getUser_token());
        }
        return params;
    }

    public static String getUserParamsGetMethod() {
        Map<String, String> params = getUserParams();
        if (params == null || TextUtils.isEmpty(params.get(KEY_USER_TOKEN))) return "";
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.toString();
    }

    public static boolean checkIsGame(String url){
        if(TextUtils.isEmpty(url))return false;
        Uri uri = Uri.parse(url);
        if(uri == null)return false;
        String query = uri.getQueryParameter(CHECK_GAME_FLAG);
        if(TextUtils.isEmpty(query))return false;
        return true;
    }


    public static synchronized void releaseWebView(WebView webview) {
        if (webview != null) {
            try {
                if (webview.getParent() != null) {
                    ((ViewGroup) webview.getParent()).removeView(webview);
                }
                webview.stopLoading();
                webview.getSettings().setJavaScriptEnabled(false);
                webview.clearHistory();
                webview.clearView();
                webview.removeAllViews();
                webview.destroy();
//                System.exit(0);
            } catch (Throwable e) {
            }
        }
    }
}
