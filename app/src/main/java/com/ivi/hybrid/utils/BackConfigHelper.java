package com.ivi.hybrid.utils;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import static com.ivi.hybrid.core.cons.Cons.CONFIG_PATH;
import static com.ivi.hybrid.core.cons.Cons.FILE_URL_BEFORE;
import static com.ivi.hybrid.core.cons.Cons.HTML_FILE_PATH;

/**
 * author: Rea.X
 * date: 2017/5/25.
 */

public class BackConfigHelper {


    private static HashMap<String, String> hashMap;

    static {
        hashMap = new HashMap<>();
//        initConfig();
    }

    public static void initConfig() {
        try {
            hashMap.clear();
            File file = new File(CONFIG_PATH);
            if (file.exists()) {
                String json = getStringFromFile(file);
                LogUtil.d("backurl:::json->" + json);
                if (!TextUtils.isEmpty(json)) {
                    JSONObject jsonObject = new JSONObject(json);
                    Iterator<String> stringIterator = jsonObject.keys();
                    while (stringIterator.hasNext()) {
                        String key = stringIterator.next();
                        hashMap.put(key, jsonObject.getString(key));
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }


    public static String getBackUrl(String url) {
        LogUtil.d("backurl:::" + hashMap.toString());
        String s = getFileByUrl(url);
        String backUrl = hashMap.get(s);
        if (TextUtils.isEmpty(backUrl)) return null;
        if (backUrl.startsWith("javascript:")) {
            return backUrl;
        }
        backUrl = FILE_URL_BEFORE + HTML_FILE_PATH + backUrl;
        return backUrl;
    }

    private static String getFileByUrl(String url) {
        if (TextUtils.isEmpty(url)) return "";

        if (url.startsWith("http://") || url.startsWith("https://")) {
            int index;
            if (url.contains("?")) {
                index = url.indexOf("?");
                url = url.substring(0, index);
            }
            if (url.contains("#")) {
                index = url.indexOf("#");
                url = url.substring(0, index);
            }
            index = url.lastIndexOf("/");
            url = url.substring(index + 1, url.length());
            return url;
        } else if (url.startsWith("file:///")) {
            int index;
            if (url.contains("#")) {
                index = url.indexOf("#");
                url = url.substring(0, index);
            }

            index = url.lastIndexOf("/", url.lastIndexOf("/") - 1);
            if (index <= 0) return null;
            url = url.substring(index + 1);
            if (url.contains("?")) {
                int i = url.indexOf("?");
                return url.substring(0, i);
            }
            return url;
        }

        return "";
    }



    private static String getStringFromFile(File file) throws IOException {
        if (file == null || !file.exists()) return null;
        FileInputStream stream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        stream.close();
        br.close();
        return sb.toString();
    }
}
