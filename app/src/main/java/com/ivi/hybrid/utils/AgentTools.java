package com.ivi.hybrid.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.modules.driver.ChannelModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.internal.Util;
import okio.BufferedSource;
import okio.Okio;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.CHANNEL_ID_JSON;

public class AgentTools {

    //如果是代理包的话，parentid=fullAgent
    public static final String AGENT_CHANNEL_KEY = "fullAgent";

    public static String getChannelID(Application application) {
        return getIDFromApkComment(application);
//        String parentIdJson = CallModule.invokeCacheModuleGet(CHANNEL_ID_JSON);
//        try {
//            if (TextUtils.isEmpty(parentIdJson)) {
//                return "";
//            } else {
//                ChannelModel model = JsonParse.fromJson(parentIdJson, ChannelModel.class);
//                if (model == null) {
//                    return "";
//                } else {
//                    return TextUtils.isEmpty(model.parentId) ? "" : model.parentId;
//                }
//            }
//        } catch (Throwable ignored) {
//        }
//        return "";
    }
    private static String getIDFromApkComment(Application application) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) return "";
        try {
            File file = new File(application.getPackageCodePath());
            ZipFile zipFile = new ZipFile(file);
            String comment = zipFile.getComment();
            zipFile.close();
            return comment;
        } catch (Throwable ignored) {
        }
        return "";
    }
    /**
     * 是否是代理包
     *
     * @return true:是
     */
    public static boolean isAgentPackageApk(Application application) {
        String channelID = getChannelID(application);
        return !TextUtils.isEmpty(channelID) && channelID.equalsIgnoreCase(AGENT_CHANNEL_KEY);
    }



    /*public static Completable agent(Context context) {
        return Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        AssetManager mg = context.getResources().getAssets();
                        InputStream is = null;
                        try {
                            is = mg.open("app.json");
                            //File exists so do something with it
                            BufferedSource source = Okio.buffer(Okio.source(is));
                            String parentIdJson = source.readUtf8();
                            CallModule.invokeCacheModuleSave(CHANNEL_ID_JSON, parentIdJson);
                            Util.closeQuietly(is);
                            Util.closeQuietly(source);
                            LogUtil.d("parentId----->" + parentIdJson);
                        } catch (IOException ex) {
                            //file does not exist
                            LogUtil.d("parentId----->" + "no app.json");
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }*/
}
