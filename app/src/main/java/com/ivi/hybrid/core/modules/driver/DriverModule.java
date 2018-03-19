package com.ivi.hybrid.core.modules.driver;

import android.Manifest;
import android.text.TextUtils;
import android.support.v4.app.FragmentActivity;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.modules.driver.agqj.LocalAGQJ;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.gesture.GestureManager;
import com.ivi.hybrid.ui.badger.ShortcurBadgerTools;
import com.ivi.hybrid.utils.ClipboardTools;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.Cookie;
import com.ivi.hybrid.utils.DeviceUtils;
import com.ivi.hybrid.utils.EmuCheckUtil;
import com.ivi.hybrid.utils.JsonParse;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ivi.hybrid.core.Hybrid.getSessionID;
import static com.ivi.hybrid.core.bridge.Bridge.returnJSData;
import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getFirmwareVersion;
import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getPhoneBoard;
import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getPhoneModel;

/**
 * author: Rea.X
 * date: 2017/6/14.
 */

public class DriverModule {

    private int tempUnReadMsgCount = 0;

    /**
     * 未读消息数量，用于更新桌面图标气泡
     *
     * @param requestId
     * @param data
     */
    public void IPSUnread(String requestId, String data, PostWebView webView) {
        IPSUnreadModel model = JsonParse.fromJson(data, IPSUnreadModel.class);
        if (model == null) return;
        int number = model.getNum();
        if (tempUnReadMsgCount == number) return;
        tempUnReadMsgCount = number;
        ShortcurBadgerTools.addAll(number);
    }

    public void clearCookie(String requestId, String data, PostWebView webView) {
        Cookie.clearCookie();
    }


    /**
     * 语音客服
     *
     * @param requestId
     * @param data
     */
    public void live800(String requestId, String data, PostWebView webView) {
        final UserModel model = JsonParse.fromJson(data, UserModel.class);
        if (model == null) return;
        Acp.getInstance(Hybrid.get()).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE)
                        .setDeniedMessage(Hybrid.get().getString(R.string.hybrid_premission_no_premission))
                        .setRationalMessage(Hybrid.get().getString(R.string.hybrid_premission_need_open_premission))
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                            @Override
                            public void run() {
                                MainThreadDisposable.verifyMainThread();
                                Config.live800(model);
                            }
                        });
                    }

                    @Override
                    public void onDenied(List<String> permissions) {

                    }
                });
    }


    public String deviceInfo(String requestId, String data, PostWebView webView) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("drice_id", DeviceUtils.getDeviceId());
            jsonObject.put("drice_type", "android");
            jsonObject.put("isEmulator", String.valueOf(EmuCheckUtil.isEmulator()));
            jsonObject.put("phone_board", getPhoneBoard());
            jsonObject.put("phone_model", getPhoneModel());
            jsonObject.put("phone_version", getFirmwareVersion());
        } catch (Exception e) {
            System.out.println("");
        }
        String json = jsonObject.toString();
        return returnJSData(requestId, true, json);
    }

    public void game(String requestId, final String data, PostWebView webView) {
        LocalAGQJ.toAGQJ(webView, data);
    }


    /**
     * 复制内容到剪切板
     *
     * @param requestId
     * @param data
     */
    public void copy(String requestId, final String data, PostWebView webView) {
        if (TextUtils.isEmpty(data)) return;
        CopyClipeModel model = JsonParse.fromJson(data, CopyClipeModel.class);
        if (model == null || TextUtils.isEmpty(model.getValue())) return;
        CommonUtils.copyToClip(Hybrid.get(), model.getValue());
    }


    /**
     * 开启或者关闭手势密码
     *
     * @param requestId
     * @param data
     */
    public void toggleGesture(String requestId, String data, PostWebView webView) {
        FragmentActivity activity = (FragmentActivity) webView.getContext();
        GestureManager.showGestureSetting(activity);
    }

    /**
     * 重置手势密码
     *
     * @param requestId
     * @param data
     */
    public void resetGesture(String requestId, String data, PostWebView webView) {
        FragmentActivity activity = (FragmentActivity) webView.getContext();
        GestureManager.showGestureValidation(activity);
    }

    /**
     * 检测app更新
     *
     * @param requestId
     * @param data
     */
    public void checkUpdate(String requestId, String data, PostWebView webView) {
        // TODO: 2017/11/8 需要更新模块支持 
    }

    public String getVersion(String requestId, String data, PostWebView webView) {
        // TODO: 2017/11/8 需要更新模块支持
        Map<String, String> map = new HashMap<>();
        map.put("version", CommonUtils.getVersionName(Hybrid.get()));
//        map.put("update", Update.getUpdateType() != NO_UPDATE ? "true" : "false");
        return returnJSData(UUID.randomUUID().toString(), true, new JSONObject(map).toString());
    }


    /**
     * 获取会话id
     *
     * @param requestId
     * @param data
     * @return
     */
    public String getSessionId(String requestId, String data, PostWebView webView) {
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", getSessionID());
        return returnJSData(UUID.randomUUID().toString(), true, new JSONObject(map).toString());
    }

    public String getParentId(String requestId, String data, PostWebView webView) {
//        {"parentId":"fullAgent"}
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parentId", Hybrid.getChannelID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJSData(UUID.randomUUID().toString(), true, jsonObject.toString());
    }
    public String getPalCode(String requestId, String data, PostWebView webView){
        Map<String, String> map = new HashMap<>();
        map.put("palCode", ClipboardTools.getID());
        return returnJSData(UUID.randomUUID().toString(), true, new JSONObject(map).toString());
    }
}
