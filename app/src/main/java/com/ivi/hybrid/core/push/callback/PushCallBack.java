package com.ivi.hybrid.core.push.callback;

import android.content.Context;

import com.ivi.hybrid.core.modules.cache.PushPreferenceHelper;
import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.core.push.heartbeat.HeartbeatRequestBody;
import com.ivi.hybrid.core.push.helper.PushHelper;
import com.ivi.hybrid.core.push.helper.UUIDUtils;
import com.ivi.hybrid.core.push.requests.RequestBody;
import com.ivi.hybrid.core.push.utils.Message;
import com.ivi.hybrid.core.push.utils.Utils;
import com.ivi.hybrid.utils.LogUtil;


/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class PushCallBack extends AsyncCallback {

    private Context context;
    private PushHelper push;
    private HeartbeatRequestBody mScheduleRequestBody;

    public PushCallBack(Context context, HeartbeatRequestBody mScheduleRequestBody, PushHelper push) {
        this.context = context;
        this.mScheduleRequestBody = mScheduleRequestBody;
        this.push = push;
    }





    @Override
    public void onSuccess(byte[] t) {
        Utils.logByteArray("接收数组》》", t);
        boolean isUUid = UUIDUtils.checkIsUUIDPackage(t[0]);
        if (isUUid) {
            if (!UUIDUtils.isUsedUUID(t)) {
                PushPreferenceHelper.clearUUID();
            } else {
                PushPreferenceHelper.saveUUIDBytes(UUIDUtils.getUUIDBytes(t));
            }
            push.refreshData(mScheduleRequestBody);
        } else {
            Message.parse(context, t);
        }
    }


    @Override
    public void onFailure(Error error) {
        LogUtil.e("失败》》》》》" + error.toString());
    }

    @Override
    public void onSend(byte[] t) {
//        Utils.logByteArray("发送数组", t);
    }

    @Override
    public void onBeforeSend(byte[] t) {
        Utils.logByteArray("准备发送数组", t);
//        push.refreshData(mScheduleRequestBody);
    }
}
