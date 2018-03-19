package com.ivi.hybrid.core.webview.x5.clients.cache;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.net.download.DownLoad;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.MD5;
import com.ivi.hybrid.utils.MimeType;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: Rea.X
 * date: 2017/11/17.
 */

public class CacheUtils {

    private CacheUtils() {
    }

    public static final String CACHE_FOLDER = "webCache";
    private static final String TAG = "CacheUtils LOG::";
    private static final String XML = "text/xml";
    private static final String HTML = "text/html";
    private static final String GIF = "image/gif";
    private static final String JPEG = "image/jpeg";
    private static final String CSS = "text/css";
    private static final String PNG = "image/png";
    private static final String JS = "application/x-javascript";
    private static final String BMP = "application/x-bmp";

    private static final String TTF = "ttf";
    private static final String EOT = "eot";
    private static final String WOFF = "woff";
    private static final String WOFF2 = "woff2";


    /**
     * 是否需要缓存
     *
     * @param url url
     * @return true:需要缓存
     */
    private static boolean needCache(String url) {
        if (!DomainHelper.isCDNHost(url)) return false;
        String extension = getUrlExtension(url);
        if (TextUtils.isEmpty(extension)) return false;
        String mimeType = MimeType.getMimeType(extension);
        if (TextUtils.isEmpty(mimeType)) return false;
        return mimeType.equalsIgnoreCase(XML) ||
//                mimeType.equalsIgnoreCase(HTML) ||
                mimeType.equalsIgnoreCase(GIF) ||
                mimeType.equalsIgnoreCase(JPEG) ||
                mimeType.equalsIgnoreCase(CSS) ||
                mimeType.equalsIgnoreCase(PNG) ||
                mimeType.equalsIgnoreCase(JS) ||
                mimeType.equalsIgnoreCase(BMP) ||
                extension.equalsIgnoreCase(TTF) ||
                extension.equalsIgnoreCase(EOT) ||
                extension.equalsIgnoreCase(WOFF) ||
                extension.equalsIgnoreCase(WOFF2)
                ;
    }

    /**
     * 根据url获取mimetype
     *
     * @param url url
     * @return mimetype
     */
    private static String getMimeType(String url) {
        if (TextUtils.isEmpty(url)) return null;
        String fileExtension = getUrlExtension(url);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    private static String getMimeTypeByExtension(String extension) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private static String getUrlExtension(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    /**
     * 这个url是否需要缓存
     *
     * @param url url
     * @return null:不需要缓存
     */
    private static boolean checkNeedCache(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))
            return false;
        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) return false;
        if (!needCache(url)) return false;
        return true;
    }

    public static WebResourceResponse cache(WebView webView, String url) {
        if (!checkNeedCache(url)) return null;
//        String extension = getUrlExtension(url);
        String filename = MD5.md5(url);
        File file = new File(CommonUtils.getCacheDir(Hybrid.get()) + File.separator + CACHE_FOLDER);
        if (!file.exists()) file.mkdirs();
        file = new File(CommonUtils.getCacheDir(Hybrid.get()) + File.separator + CACHE_FOLDER, filename);
        if (file.exists() && file.length() > 0) {
            return getResponseFromFile(url, file);
        }
        return download(webView, url, filename);
    }

    private static WebResourceResponse getResponseFromFile(String url, File file) {
        if (file.exists() && file.length() >= 0) {
            try {
                WebResourceResponse webResourceResponse = new WebResourceResponse();
                InputStream inputStream = new FileInputStream(file);
                webResourceResponse.setData(inputStream);
                webResourceResponse.setMimeType(getMimeType(url));
                webResourceResponse.setEncoding("utf-8");
                return webResourceResponse;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static WebResourceResponse download(WebView webView, String url, String filename) {
        try {
            Hybrid.getJobManager().addJobInBackground(new CacheResourceJob(webView, url, filename));
//            DownLoad.INSTANT.download(url, CommonUtils.getCacheDir(Hybrid.get()) + File.separator + CACHE_FOLDER, filename, null, false);
//            File file = DownLoad.INSTANT.downloadSync(url, path, webView);
//            if (file == null) return null;
//            return getResponseFromFile(url, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
