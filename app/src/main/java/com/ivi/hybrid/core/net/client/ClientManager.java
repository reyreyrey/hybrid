package com.ivi.hybrid.core.net.client;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.net.interceptor.ApptokenTimeoutInterceptor;
import com.ivi.hybrid.core.net.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.https.HttpsUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import static com.ivi.hybrid.core.cons.Cons.TIMEOUT;

/**
 * author: Rea.X
 * date: 2017/7/19.
 */

public class ClientManager {

    private static OkHttpClient defaultClient, checkDomainClient, downloadClient;

    private static OkHttpClient getOkHttpClient(long timeout, boolean isCheckDomain, boolean isDownload) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (!isCheckDomain && !isDownload) {
            builder.addInterceptor(new ApptokenTimeoutInterceptor());
        }

        if (Hybrid.isDebug()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("HttpLog");
            interceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            interceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(interceptor);
        }
        builder.followRedirects(isDownload);
        builder.followSslRedirects(isDownload);
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectionPool(new ConnectionPool(10, 2 * 60 * 1000, TimeUnit.MILLISECONDS));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }


    public static OkHttpClient getDefaultHttpClient() {
        if (defaultClient == null)
            defaultClient = getOkHttpClient(TIMEOUT, false, false);
        return defaultClient;
    }

    public static OkHttpClient getCheckDomainClient() {
        if (checkDomainClient == null)
            checkDomainClient = getOkHttpClient(10 * 1000, true, false);
        return checkDomainClient;
    }

    public static OkHttpClient getDownloadClient() {
        if (downloadClient == null)
            downloadClient = getOkHttpClient(TIMEOUT, false, true);
        return downloadClient;
    }


}
