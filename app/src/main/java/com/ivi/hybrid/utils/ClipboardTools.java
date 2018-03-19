package com.ivi.hybrid.utils;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.config.Config;

import java.io.File;
import java.util.zip.ZipFile;

import static com.ivi.hybrid.utils.AgentTools.AGENT_CHANNEL_KEY;

public class ClipboardTools {
    private static ClipboardManager clipboardManager;
    private static final String KEY = "aW50ZWNo-";
    private static final String KEY_ID = "key_id";

    private static final String TAG = "ClipboardTools log::::";


    /**
     * ->读取apk comm
     * @param application
     */
    public static void init(Application application) {
        String channelID = com.ivi.hybrid.core.Hybrid.getChannelID();
        if (TextUtils.isEmpty(channelID)) return;
        if (!channelID.equalsIgnoreCase(AGENT_CHANNEL_KEY)) return;
        clipboardManager = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null) return;
        ClipData data = clipboardManager.getPrimaryClip();
        if (data == null) return;
        for (int i = 0; i < data.getItemCount(); i++) {
            ClipData.Item item = data.getItemAt(i);
            if (item == null) continue;
            CharSequence charSequence = item.getText();
            if (charSequence == null) return;
            String clipeText = charSequence.toString();
            LogUtil.d(TAG + "get clipeText:::" + clipeText);
            if (!TextUtils.isEmpty(clipeText) && clipeText.startsWith(KEY)) {
                //是app存的值
                getResult(clipeText);
            }
        }
    }


    /**
     * 处理剪切板上的字符串，分离出id
     * @param clipeText 剪切板上的字符串
     */
    private static void getResult(String clipeText) {
        if (TextUtils.isEmpty(clipeText)) return;
        if (!clipeText.contains("-")) return;
        String[] strs = clipeText.split("-");
        LogUtil.d(TAG + "get clipeText strs array:::" + strs.toString());
        if (strs == null || strs.length == 0 || strs.length < 3) return;
        String projectid = strs[1];
        if (TextUtils.isEmpty(projectid)) return;
        if (!projectid.equalsIgnoreCase(Config.getProjectId())) return;
        String id = strs[2];
        LogUtil.d(TAG + "save data:::" + id);
        CallModule.invokeCacheModuleSave(KEY_ID, id);
    }

    /**
     * 获取缓存中保存的从剪切板上读取的推广id
     * @return 推广id
     */
    public static String getID() {
        String id = CallModule.invokeCacheModuleGet(KEY_ID);
        LogUtil.d(TAG + "get data:::" + id);
        return id;
    }
}
