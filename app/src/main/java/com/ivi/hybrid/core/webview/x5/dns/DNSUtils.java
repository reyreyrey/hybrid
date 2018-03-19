package com.ivi.hybrid.core.webview.x5.dns;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.hybrid.R;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.modules.forward.WebViewBundleModel;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.net.tools.AppTokenHelper;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Response;

/**
 * author: Rea.X
 * date: 2017/11/18.
 */

class DNSUtils {

    public static final String LOG_TAG = "dns tag::::";

    static WebResourceResponse getDnsResponse(WebView webView, String url) {
        LogUtil.d(LOG_TAG + "拦截到DNS请求---开始校验" + url);
        Uri uri = Uri.parse(url);
        if (!checkNeedUseDNS(uri)) {
            LogUtil.d(LOG_TAG + "校验结束---不拦截");
            return null;
        }
        LogUtil.d(LOG_TAG + "校验结束---开始拦截");
        List<DataModel> formData = null;
        if (PostDataCacheUtils.isPOST(url)) {
            formData = PostDataCacheUtils.get(url);
        }
        LogUtil.d(LOG_TAG + "POST的数据是：" + (formData == null ? "" : formData.toString()));
        //替换ip并取出用户信息
        String [] urls = url(uri);
        if(urls == null || urls.length != 2)return null;
        String newUrl = urls[0];
        String host = urls[1];
        LogUtil.d(LOG_TAG + "替换后的url:::" + newUrl);
        if (TextUtils.isEmpty(newUrl)) return null;
        long tempTime = System.currentTimeMillis();
        Response response = call(webView, newUrl, host, formData);
        LogUtil.d(LOG_TAG + "请求耗时：" + (System.currentTimeMillis() - tempTime));
        tempTime = System.currentTimeMillis();
        if (response == null) return null;
        int responseCode = response.code();
        if (responseCode == 404) return null;
        if (postRedirect(webView, newUrl, response)) return null;
        if (responseCode == 302) return null;
        String[] mimeEncode = getContentTypeMimeType(response);
        if (mimeEncode == null) return null;
        String mimeType = mimeEncode[0];
        String encoding = mimeEncode[1];
        if (TextUtils.isEmpty(mimeType)) return null;
        if (TextUtils.isEmpty(encoding)) encoding = "utf-8";
        InputStream inputStream = response.body().byteStream();
        inputStream = injection(webView, inputStream, mimeType, encoding);
        LogUtil.d(LOG_TAG + "注入耗时：" + (System.currentTimeMillis() - tempTime));
        if (inputStream == null) return null;
        return new WebResourceResponse(mimeType, encoding, inputStream);
    }

    private static InputStream injection(WebView webView, InputStream inputStream, String mimeType, String encoding) {
        try {
            byte[] pageContents = readFully(inputStream);
            if (mimeType.equals("text/html")) {
                String result = Injection.injectionJS(webView.getContext(), pageContents);
                if (!TextUtils.isEmpty(result)) {
                    pageContents = result
                            .getBytes(encoding);
                }

            }
            return new ByteArrayInputStream(pageContents);
        } catch (Exception e) {
        }
        return null;
    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    private static String[] getContentTypeMimeType(Response response) {
        String contentType = response.header("Content-type");
        String[] strs = new String[2];
        String mimeType, encoding;
        if (!TextUtils.isEmpty(contentType)) {
            if (contentType.contains(";")) {
                String[] args = contentType.split(";");
                mimeType = args[0];
                String[] args2 = args[1].trim().split("=");
                if (args.length == 2 && args2[0].trim().toLowerCase().equals("charset")) {
                    encoding = args2[1].trim();
                } else {
                    encoding = "utf-8";
                }
            } else {
                mimeType = contentType;
                encoding = "utf-8";
            }
            strs[0] = mimeType;
            strs[1] = encoding;
            return strs;
        }
        return null;
    }

    /**
     * 重定向处理
     *
     * @param webView
     * @param response1
     * @return
     */
    private static boolean postRedirect(final WebView webView, String url, Response response1) {
        if (response1.isRedirect()) {
            if (!isHtml(url)) return false;
            List<String> cookies = response1.headers("Set-Cookie");
            ArrayList<String> cks = cookie(cookies);
            String location = response1.header("Location");
            if (location.contains("app_error.htm")) {
                finishWebView(webView, true);
                return false;
            }
            Uri uri = Uri.parse(location);
            String schem = uri.getScheme();
            if (TextUtils.isEmpty(schem)) {
                location = checkurl(location);
            }
            final WebViewBundleModel bundleModel;
            if (!DomainHelper.isWebHost(location)) {
                bundleModel = new WebViewBundleModel(location, true, -1, true, -1, cks);
            } else {
                bundleModel = new WebViewBundleModel(location, false, -1, false, -1, cks);
            }
            AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    MainThreadDisposable.verifyMainThread();
                    HybridWebViewActivity.openWebView(bundleModel, false);
                }
            });
            return true;
        }
        return false;
    }

    private static void finishWebView(final WebView webView, final boolean isShowError) {
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                webView.stopLoading();
                if (isShowError) {
                    ToastUtils.toastError(webView.getContext(), webView.getContext().getString(R.string.hybrid_common_net_error));
                }
                Context context = webView.getContext();
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            }
        });
    }

    private static ArrayList<String> cookie(List<String> cookies) {
        ArrayList<String> params = new ArrayList<>();
        for (String cookie : cookies) {
            if (TextUtils.isEmpty(cookie)) continue;
            if (!cookie.contains(";")) {
                params.add(cookie);
            } else {
                split(params, cookie);
            }
        }
        return params;
    }

    private static void split(List<String> params, String str) {
        if (str.contains(";")) {
            String[] strs = str.split(";");
            if (strs.length > 1) {
                for (String s : strs) {
                    params.add(s);
                }
            }
        }
    }

    private static String checkurl(String url) {
        String host = DomainHelper.getCanUsedDomain().domain;
        if (!TextUtils.isEmpty(host)) {
            String prol = "";
            if (!host.startsWith("http://") && !host.startsWith("https://"))
                prol = "https://";
            else {
                prol = Uri.parse(host).getScheme() + "://";
            }
            if (TextUtils.isEmpty(prol))
                prol = "https://";
            String domain = DomainHelper.getCanuserHost();
            if (domain.substring(domain.length() - 1).equals("/")) {
                if (url.substring(0, 1).equals("/")) {
                    url = url.substring(1, url.length());
                }
            } else if (!url.substring(0, 1).equals("/")) {
                url = "/" + url;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = domain + url;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = prol + url;
            }
            if (url.startsWith("http://http://")) {
                url = url.replaceAll("http://http://", "http://");
            }
            if (url.startsWith("https://https://")) {
                url = url.replaceAll("https://https://", "https://");
            }

            return url;
        }
        return null;
    }

    private static boolean isHtml(String url) {
        try {
            Uri u = Uri.parse(url);
            String path = u.getPath();
            return path.endsWith("htm") || path.endsWith("html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * okhttp请求
     *
     * @param webView webview
     * @param url     请求的原始url
     * @param host    需要携带的host header头
     * @return response
     */
    private static Response call(WebView webView, String url, String host, List<DataModel> formData) {
        try {
            String userAgent = webView.getSettings().getUserAgentString();
            Request request = null;
            if (!isHtml(url)) {
                //请求资源
                request = OkGo
                        .<String>get(url);
            } else if (formData != null && formData.size() == 0) {
                //ajax get请求
                request = OkGo
                        .<String>get(url)
                        .params(getUserParams());
            } else {
                request = OkGo
                        .<String>post(url)
                        .params(getUserParams());
                if (formData != null && formData.size() != 0) {
                    for (DataModel m : formData) {
                        request.params(m.getName(), URLDecoder.decode(m.getValue()), false);
                    }
                    formData.clear();
                    formData = null;
                }
            }
            request = request
                    .headers("Host", host)
                    .headers("User-Agent", userAgent);
            return request.execute();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 检测是否需要拦截并走DNS
     *
     * @param uri 需要检测url
     * @return true:需要拦截
     */
    private static boolean checkNeedUseDNS(Uri uri) {
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (scheme == null) return false;
        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) return false;
        if (!Config.isOpenDNS()) return false;
        if (!DomainHelper.isWebHost(uri.toString())) return false;
        return true;
    }

    /**
     * 获取post用户数据
     *
     * @return map
     */
    private static Map<String, String> getUserParams() {
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
            params.put("accountName", userModel.getLogin_name());
            params.put("appToken", appToken);
            params.put("userToken", userModel.getUser_token());
        }
        return params;
    }

    /**
     * 替换ip并取出用户信息
     *
     * @param uri
     * @return
     */
    private static String[] url(Uri uri) {
        String url = uri.toString();
        String host = uri.getHost();
        //将host替换为ip
        String ip = DomainHelper.getChangeIP(host);
        if (TextUtils.isEmpty(ip) && !DomainHelper.isIP(host)) return null;
        if (DomainHelper.isIP(host)) {
            host = DomainHelper.getHostHeader();
        }

        //去除用户信息
        url = getReplaceUsermsgUrl(url);
        if (ip != null) {
            ip = ip.replaceAll("http://", "").replaceAll("https://", "").replaceAll("/", "");
            url = url.replaceAll(host, ip);
        }
        return new String[]{url, host};
    }

    /**
     * 将url中的用户信息去除
     *
     * @param url
     * @return
     */
    private static String getReplaceUsermsgUrl(String url) {
        String mao = null;
        if (url.contains("#")) {
            int index2 = url.indexOf("#");
            mao = url.substring(index2);
        }

        String postData = getPostData(url);
        url = url.replaceAll(postData, "");
        if (!TextUtils.isEmpty(mao))
            url = url + mao;
        if (url.endsWith("&") || url.endsWith("?")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 获取url串中的用户信息
     *
     * @param url
     * @return
     */
    private static String getPostData(String url) {
        int index = url.indexOf("?");
        if (index < 0) return "";
        String params = url.substring(index + 1);
        if (TextUtils.isEmpty(params)) return "";
        String[] pams = params.split("\\&");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pams.length; i++) {
            String str = pams[i];
            if (TextUtils.isEmpty(str) || !str.contains("=")) continue;
            String key = str.substring(0, str.indexOf("="));
            if (key.equals("userToken") || key.equals("appToken") || key.equals("accountName") || key.equals("accountToken")) {
                builder.append(str);
                builder.append("&");
            }
        }
        String s = builder.toString();
        if (TextUtils.isEmpty(s)) return "";
        return builder.toString().substring(0, builder.toString().length() - 1);
    }
}
