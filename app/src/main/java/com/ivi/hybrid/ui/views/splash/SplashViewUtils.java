package com.ivi.hybrid.ui.views.splash;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.domain.DnsCNDModel;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.net.HybridRequest;
import com.ivi.hybrid.core.net.models.InterceptModel;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.unzip.Unzip;
import com.ivi.hybrid.utils.BackConfigHelper;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.FlurryHelper;
import com.ivi.hybrid.utils.JsonParse;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.VersionTools;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.mylhyl.circledialog.CircleDialog2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.CDN;
import static com.ivi.hybrid.ui.views.splash.SplashView.TIME_TAG;
import static com.ivi.hybrid.utils.SplashTools.copy;
import static com.ivi.hybrid.utils.activity_manager.ActivityManager.getTagActivity;

/**
 * author: Rea.X
 * date: 2017/11/6.
 */

class SplashViewUtils {
    private static final String NULL = "1";
    private static long domainGetTime = System.currentTimeMillis();

    /**
     * 复制解压压缩包
     *
     * @return Observable
     */
    static Observable<String> unzip() {
        final long copyTime = System.currentTimeMillis();
        return Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        File file = copy();
                        LogUtil.d(TIME_TAG + "复制耗时：" + (System.currentTimeMillis() - copyTime));
                        if (file == null) {
                            return NULL;
                        }
                        return file.getAbsolutePath();
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String filePath) throws Exception {
                        if (filePath.equalsIgnoreCase(NULL)) return NULL;
                        File f = new File(filePath);
                        long zipTime = System.currentTimeMillis();
                        FlurryHelper.INSTANT.onZipStart();
                        Unzip.unzip(f.getParent() + File.separator, f.getAbsolutePath());
                        FlurryHelper.INSTANT.onZipEnd(String.valueOf((System.currentTimeMillis() - zipTime)));
//                        SystemIo.unzip(new File(f.getParent()), f.getAbsolutePath());
//                        ZipUtils.unzipFile(f.getAbsolutePath(), f.getParent());
                        LogUtil.d(TIME_TAG + "解压耗时：" + (System.currentTimeMillis() - copyTime));
                        return f.getAbsolutePath();
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String filePath) throws Exception {
                        if (filePath.equalsIgnoreCase(NULL)) return NULL;
                        BackConfigHelper.initConfig();
                        return filePath;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String filePath) throws Exception {
                        if (filePath.equalsIgnoreCase(NULL)) return NULL;
                        File file = new File(filePath);
                        try {
                            file.delete();
                        } catch (RuntimeException e) {
                            System.out.println("RuntimeException");
                        }
                        VersionTools.saveAppVersion(CommonUtils.getVersionName(Hybrid.get()));
                        return filePath;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 检测域名是否可用
     */
    @SuppressLint("CheckResult")
    static void checkDomain(final OnDomaingetCallback callback) {
        domainGetTime = System.currentTimeMillis();
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(@NonNull String s) throws IOException {
                        return HybridRequest.request(null, "app/getCDNHostDns");
                    }
                })
                .map(new Function<Response, String>() {
                    @Override
                    public String apply(@NonNull Response response) throws Exception {
                        String result = response.body().string();
                        InterceptModel interceptModel = JsonParse.fromJson(result, InterceptModel.class);
                        if (interceptModel == null || !interceptModel.status) {
                            return NULL;
                        }
                        ResultModel<DnsCNDModel> resultModel = JsonParse.fromJson(result, new TypeToken<ResultModel<DnsCNDModel>>() {
                        }.getType());
                        if (resultModel != null && resultModel.status) {
                            DnsCNDModel model = resultModel.data;
                            CallModule.invokeCacheModuleSave(CDN, model.getCdnHost());
                            List<DnsCNDModel.DnsBean> dns = model.getDns();
                            if (dns != null && dns.size() != 0) {
                                LogUtil.d(TIME_TAG + "获取domain耗时：" + (System.currentTimeMillis() - domainGetTime));
                                FlurryHelper.INSTANT.onDomainEndRequest("", true, System.currentTimeMillis() - domainGetTime);
                                FlurryHelper.INSTANT.onDomainStartCheckRequest();
                                domainGetTime = System.currentTimeMillis();
                                DomainHelper.start(model);
                                return "666";
                            }
                        }
                        FlurryHelper.INSTANT.onDomainEndRequest("", false, System.currentTimeMillis() - domainGetTime);
                        return NULL;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String domain = DomainHelper.getCanuserHost();
                        if (TextUtils.isEmpty(domain)) {
                            //获取domain失败
                            FlurryHelper.INSTANT.onDomainCheckEndRequest("", false, System.currentTimeMillis() - domainGetTime);
                            showNoDomainDialog(callback);
                            return;
                        }
                        if (callback != null)
                            callback.onGetSuccess();
                        FlurryHelper.INSTANT.onDomainCheckEndRequest(domain, true, System.currentTimeMillis() - domainGetTime);
                        LogUtil.d(TIME_TAG + "检测domain耗时：" + (System.currentTimeMillis() - domainGetTime));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        LogUtil.e("'domain error::::" + throwable.getMessage());
                        showNoDomainDialog(callback);
                    }
                });
    }

    //当未获取到domain或者domain不可用时，弹出错误窗口
    private static void showNoDomainDialog(final OnDomaingetCallback callback) {
        try {
            FragmentActivity activity = (FragmentActivity) getTagActivity();
            new CircleDialog2.Builder(activity)
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .setTitle(activity.getString(R.string.hybrid_alert_tip))
                    .setText(activity.getString(R.string.hybrid_common_net_error))
                    .setNegative(activity.getString(R.string.hybrid_common_exit), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityManager.exitApp();
                        }
                    })
                    .setPositive(activity.getString(R.string.hybrid_common_retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkDomain(callback);
                        }
                    })
                    .show();
        } catch (Exception e) {
            System.out.println("");
        }
    }


    interface OnDomaingetCallback {
        void onGetSuccess();
    }
}
