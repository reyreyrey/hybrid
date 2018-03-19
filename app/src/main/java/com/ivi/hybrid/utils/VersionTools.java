package com.ivi.hybrid.utils;

import android.telecom.Call;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.LOCO_APP_VERSION;
import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.VERSION;

/**
 * author: Rea.X
 * date: 2017/5/30.
 */

public class VersionTools {

    /**
     * 获取增量包版本号
     * @return
     */
    public static String getDeltaVersion(){
        String veriosn = CallModule.invokeCacheModuleGet(VERSION);
        if(TextUtils.isEmpty(veriosn)){
            veriosn = CommonUtils.getVersionName(Hybrid.get());
        }
        LogUtil.d("保存：：：：：获取：：："+veriosn);
        return veriosn;
    }

    /**
     * 保存增量包版本号
     * @param version
     */
    public static void saveDeltaVersion(String version){
        LogUtil.d("保存：：：：："+version);
        CallModule.invokeCacheModuleSave(VERSION, version);
    }


    /**
     * 获取增量包版本号
     * @return
     */
    public static String getAppVersion(){
        return CallModule.invokeCacheModuleGet(LOCO_APP_VERSION);
    }

    /**
     * 保存增量包版本号
     * @param version
     */
    public static void saveAppVersion(String version){
        CallModule.invokeCacheModuleSave(LOCO_APP_VERSION, version);
    }
}
