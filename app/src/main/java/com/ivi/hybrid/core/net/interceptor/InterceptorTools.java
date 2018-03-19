package com.ivi.hybrid.core.net.interceptor;

import android.support.annotation.NonNull;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.key.ApiKey;
import com.ivi.hybrid.core.net.tools.Sign;
import com.lzy.okgo.request.base.ProgressRequestBody;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.Util.UTF_8;

/**
 * author: Rea.X
 * date: 2017/11/1.
 */

public class InterceptorTools {

//    public static FormBody interceptorRequestParams(Request request) {
//        RequestBody body = request.body();
//        if (body == null) return null;
//        if (body instanceof FormBody) {
//            return getFormBody((FormBody) body);
//        }
//        return null;
//    }

    @NonNull
    public static FormBody getFormBody(Map<String, String> params) {
        params = Sign.getParams(params);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            while (true) {
                String decodeValue = URLDecoder.decode(value);
                if (decodeValue.equalsIgnoreCase(value)) {
                    value = decodeValue;
                    break;
                }
                value = decodeValue;
            }
            builder.add(entry.getKey(), value);
        }
        return builder.build();
    }

    /**
     * 获取请求AppToken的RequestBody
     *
     * @return
     */
    public static Map<String, String> getRequestAppTokenParams() {
        Map<String, String> map = new HashMap<>();
        map.put("app_id", ApiKey.getAppId(Hybrid.getContext()));
        map.put("app_sign", Config.getAppSign());
        map.put("package_name", Config.getPackageName());
        map.put("platform", Config.getPaltform());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("signature", Sign.sign(map));
        return map;
    }

    public static FormBody getRequestAppTokenBody() {
        Map<String, String> params = getRequestAppTokenParams();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public static String getResponseString(Response response) {
        ResponseBody responseBody = null;
        BufferedSource source = null;
        Buffer buffer = null;
        try {
            responseBody = response.body();
            source = responseBody.source();
            source.request(Integer.MAX_VALUE);
            buffer = source.buffer();
            Charset charset = UTF_8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset();
                if (charset == null) charset = UTF_8;
            }
            return buffer.clone().readString(charset);
        } catch (Exception e) {
        } finally {
//            try {
//                if (response != null)
//                    response.close();
//                if (responseBody != null)
//                    responseBody.close();
//                if (source != null)
//                    source.close();
//                if (buffer != null)
//                    buffer.close();
//            } catch (Throwable e) {
//            }
        }
        return null;
    }
}
