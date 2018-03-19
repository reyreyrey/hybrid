package com.ivi.hybrid.core.net.tools;

import android.text.TextUtils;


import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.key.ApiKey;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.MD5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/3/15.
 */

public class Sign {


    public static String sign(Map<String, String> params) {
        int i = 0;
        String user_token = null;
        if (params.containsKey("user_token")) {
            user_token = params.remove("user_token");
        }
        String[] strs = new String[params.size()];
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            strs[i] = key;
            i++;
        }
        Arrays.sort(strs);
        StringBuilder sb = new StringBuilder();
        int position = 0;
        for (String key : strs) {
            position++;
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
            sb.append("&");
        }
        sb.append("app_key=" + ApiKey.getAppKey(Hybrid.getContext(), Hybrid.getRunModel()));
        LogUtil.d("sign:" + sb.toString());
        if (user_token != null) {
            params.put("user_token", user_token);
        }
        return MD5.md5(sb.toString());
    }


    public static Map<String, String> getParams(Map<String, String> params) {
        if (params == null)
            params = new HashMap<>();
        String appToken = AppTokenHelper.INSTANT.getAppToken();
        if (!TextUtils.isEmpty(appToken))
            params.put("app_token", appToken);
        params.put("signature", Sign.sign(params));
        String userToken = UserHelper.INSTANT.getUserToken();
        if (!TextUtils.isEmpty(userToken))
            params.put("user_token", userToken);
        return params;
    }
}
