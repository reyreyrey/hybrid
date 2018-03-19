package com.ivi.hybrid.utils;

import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * author: Rea.X
 * date: 2017/11/20.
 */

public class MimeType {

    private static HashMap<String, String> map;

    static {
        read();
    }

    private static void read() {
        map = new HashMap<>();
        try {
            InputStream inputStream = Hybrid.get().getAssets().open("mimeType.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
            br.close();

            String json = sb.toString();
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 通过后缀获取mimeType
     *
     * @param extension 后缀
     * @return mimeType
     */
    public static String getMimeType(String extension) {
        if (map == null) read();
        if (TextUtils.isEmpty(extension)) return null;
        if (map.containsKey(extension)) return map.get(extension);
        return null;
    }
}
