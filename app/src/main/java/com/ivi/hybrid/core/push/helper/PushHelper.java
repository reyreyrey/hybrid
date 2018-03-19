package com.ivi.hybrid.core.push.helper;

import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.core.push.callback.Callback;
import com.ivi.hybrid.core.push.requests.Request;
import com.ivi.hybrid.core.push.requests.ScheduleRequest;
import com.ivi.hybrid.core.push.requests.ScheduleRequestBody;
import com.ivi.hybrid.utils.LogUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * author: Rea.X
 * date: 2017/11/6.
 */

public class PushHelper {
    public static int DEFAULT_COREPOOLSIZE = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4));
    public static int DEFAULT_TIMEOUT = 15000;
    private static ScheduledExecutorService sScheduleExecutor;
    private static volatile PushHelper sUdpClient;
    private Request sRequest;
    private ScheduleRequest sScheduleRequest;

    public static PushHelper getInstance() {
        if (sUdpClient == null) {
            sUdpClient = new PushHelper();
        }
        return sUdpClient;
    }

    private PushHelper() {
        stop();
        sScheduleExecutor = Executors.newScheduledThreadPool(DEFAULT_COREPOOLSIZE);
        sRequest = null;
        sScheduleRequest = null;
    }

    public void newScheduleRequest(ScheduleRequestBody requestBody, Callback callback) {
        LogUtil.d("newScheduleRequest:sendPacket.getData()");
        if (sScheduleRequest == null) {
            sScheduleRequest = new ScheduleRequest(requestBody, callback);
            sScheduleExecutor.scheduleAtFixedRate(sScheduleRequest, requestBody.getDelayed(), requestBody.getPeriod(), requestBody.getTimeUnit());
        } else {
            sScheduleRequest.setData(requestBody.getBody());
            sScheduleExecutor.scheduleAtFixedRate(sScheduleRequest, requestBody.getDelayed(), requestBody.getPeriod(), requestBody.getTimeUnit());
        }

    }

    public void refreshData(ScheduleRequestBody requestBody) {
        if (sScheduleRequest != null) {
            requestBody.refreshBody();
            sScheduleRequest.setData(requestBody.getBody());
        }
    }


    public void stop() {
        if (sScheduleExecutor != null) {
            sScheduleExecutor.shutdownNow();
        }
    }
}
