package com.ivi.hybrid.core.push.heartbeat;

import android.content.Context;

import com.ivi.hybrid.core.push.requests.ScheduleRequestBody;

import java.util.concurrent.TimeUnit;

import static com.ivi.hybrid.core.push.utils.Cons.HEART_BEAT_TIME;

/**
 * author: Rea.X
 * date: 2017/11/6.
 */

public class HeartbeatRequestBody extends ScheduleRequestBody {
    public HeartbeatRequestBody(Context context) {
        super(context, 0, HEART_BEAT_TIME, TimeUnit.MILLISECONDS);
    }
}
