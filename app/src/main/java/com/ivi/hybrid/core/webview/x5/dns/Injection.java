package com.ivi.hybrid.core.webview.x5.dns;

import android.content.Context;


import com.ivi.hybrid.utils.LogUtil;

import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: Rea.X
 * date: 2017/9/26.
 */

public class Injection {
    private static String mInterceptHeader = null;

    /**
     * 将JS注入到网页
     *
     * @param context
     * @param data
     * @return
     * @throws IOException
     */
    public static String injectionJS(Context context, byte[] data) throws IOException {
        if (mInterceptHeader == null) {
            mInterceptHeader = new String(readFully(context.getAssets().open(
                    "interceptheader.html")), "utf-8");
        }

        String html = new String(data, "utf-8");
        if(!html.contains("<html>") && !html.startsWith("<!DOCTYPE html>"))return null;
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        doc.outputSettings().prettyPrint(true);
        doc.outputSettings().charset("utf-8");
        org.jsoup.select.Elements el = doc.getElementsByTag("head");
        if (el.size() > 0) {
            el.get(0).prepend(mInterceptHeader);
        }
        LogUtil.d("pageContents::::"+doc.toString());
        return doc.toString();
    }


    public static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
