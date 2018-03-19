package com.ivi.hybrid.core.push;

import android.content.Context;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.push.heartbeat.HeartbeatHelper;
import com.ivi.hybrid.core.push.utils.Utils;


public final class Push {

    private static Push push;

    public static Push getInstance() {
        if (push == null) {
            push = new Push();
        }
        return push;
    }



    public void start(Context context) {
        Utils.startService(context);
    }

    /**
     * 登录状态改变
     * @param isLogin
     */
    public void loginStatusChanged(boolean isLogin){
        HeartbeatHelper.shouldRefreshData();
    }

    /**
     * app切换前后台
     * @param isFront
     */
    public void appSwitchbackOrFront(boolean isFront){
        HeartbeatHelper.shouldRefreshData();
    }


}
