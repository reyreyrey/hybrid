package com.ivi.hybrid.core.net.interceptor;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.net.models.TokenModel;
import com.ivi.hybrid.core.net.tools.AppTokenHelper;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.ivi.hybrid.core.net.interceptor.InterceptorTools.getFormBody;
import static com.ivi.hybrid.core.net.interceptor.InterceptorTools.getRequestAppTokenBody;
import static com.ivi.hybrid.core.net.interceptor.InterceptorTools.getResponseString;

/**
 * author: Rea.X
 * date: 2017/11/1.
 * 拦截appToken过期
 */

public class ApptokenTimeoutInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        Response response = chain.proceed(originRequest);
//        FormBody originBody = interceptorRequestParams(originRequest);
        HttpUrl originUrl = chain.request().url();
        try {
            String bodyString = getResponseString(response);
            if (bodyString == null) return response;
            InterceptModel model = JsonParse.fromJson(bodyString, InterceptModel.class);
            if (model != null && isAppTokenTimeOut(model)) {
                //发起一次请求appToken的请求
                requestAppToken(chain);
                //原始的请求参数
                Map<String, String> originParams = bodyToString(originRequest);
                FormBody body = getFormBody(originParams);
                Request newRequest = chain.request().newBuilder().url(originUrl).post(body).build();
                return chain.proceed(newRequest);
            }
        } catch (Exception e) {
            LogUtil.e("ApptokenTimeoutInterceptor:" + e.toString());
        }
        return response;
    }


    private Map<String, String> bodyToString(Request request) {
        Map<String, String> map = new HashMap<>();
        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) return map;
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = getCharset(body.contentType());
            String fragment = buffer.readString(charset);
            buffer.close();
            if (TextUtils.isEmpty(fragment)) return map;

            fragment = "http://xxx.com/a.html?" + fragment;
            Uri uri = Uri.parse(fragment);
            if (uri == null || uri.getQueryParameterNames() == null) return map;
            for (String key : uri.getQueryParameterNames()) {
                map.put(key, uri.getQueryParameter(key));
            }
            return map;
        } catch (Exception e) {
            System.out.println("");
        }
        return map;
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }

    /**
     * 请求新的appToken
     *
     * @param chain
     * @throws IOException
     */
    private void requestAppToken(Chain chain) throws IOException {
        HttpUrl httpUrl = chain.request().url();
        Uri.Builder builder = new Uri.Builder();
        if (httpUrl.port() == 80) {
            builder.encodedAuthority(httpUrl.host());
        } else {
            builder.encodedAuthority(httpUrl.host() + ":" + httpUrl.port());
        }
        builder.scheme(httpUrl.scheme());
        builder.path("auths/appToken");
        Request appTokenRequest = chain.request().newBuilder().url(builder.build().toString()).post(getRequestAppTokenBody()).build();
        Response appTokenResponse = chain.proceed(appTokenRequest);
        String appTokenResponseString = getResponseString(appTokenResponse);
        InterceptModel model = JsonParse.fromJson(appTokenResponseString, InterceptModel.class);
        if (model != null && !model.status) return;
        ResultModel<TokenModel> appTokenModel = JsonParse.fromJson(appTokenResponseString, new TypeToken<ResultModel<TokenModel>>() {
        });
        if (appTokenModel != null)
            AppTokenHelper.INSTANT.saveAppToken(appTokenModel.data);
    }


    /**
     * 检测AppToken是否过期
     *
     * @param model
     * @return
     */
    private static boolean isAppTokenTimeOut(InterceptModel model) {
        if (model != null && !model.isStatus() && model.getCodeHttp() == 403) {
            return true;
        }
        return false;
    }
}
