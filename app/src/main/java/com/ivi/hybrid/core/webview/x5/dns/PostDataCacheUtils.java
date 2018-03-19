package com.ivi.hybrid.core.webview.x5.dns;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Rea.X
 * date: 2017/9/22.
 */

public class PostDataCacheUtils {

    private static final int MAX_SIZE = 50;
    private static LruCache<String, List<DataModel>> lruCache;

    static {
        lruCache = new LruCache<>(MAX_SIZE);
    }

    public static void save(String key, List<DataModel> value) {
        lruCache.put(key, value);
    }

    public static void save(String key, String value) {
        List<DataModel> lists = new ArrayList<>();
        if (TextUtils.isEmpty(value)) {
            lruCache.put(key, lists);
            return;
        }
        if (value.contains("&")) {
            String[] kv = value.split("\\&");
            if (kv != null && kv.length != 0) {
                for (String s : kv) {
                    splitItem(s, lists);
                }
            }
        } else {
            splitItem(value, lists);
        }
        lruCache.put(key, lists);
    }

    private static void splitItem(String s, List<DataModel> lists) {
        if (TextUtils.isEmpty(s) || !s.contains("=")) return;
        DataModel model = new DataModel();
        String[] kv = s.split("\\=");
        if (kv != null && kv.length > 1) {
            model.setName(kv[0]);
            model.setValue(kv[1]);
            lists.add(model);
        }
    }

    public static List<DataModel> get(String key) {
        return lruCache.remove(key);
    }

    /**
     * 是否是post请求
     *
     * @param url
     * @return
     */
    public static boolean isPOST(String url) {
        return lruCache.get(url) != null;
    }

}
