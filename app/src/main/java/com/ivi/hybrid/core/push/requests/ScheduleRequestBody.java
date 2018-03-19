package com.ivi.hybrid.core.push.requests;

import android.content.Context;


import com.ivi.hybrid.core.push.heartbeat.HeartbeatPackage;

import java.util.concurrent.TimeUnit;


public class ScheduleRequestBody implements IRequest{

    private byte[] body;
    public static final long DEFAULT_DELAY = 0L;
    public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.MILLISECONDS;
    private long delayed = DEFAULT_DELAY;
    private TimeUnit mTimeUnit = DEFAULT_TIMEUNIT;
    private long period;
    private HeartbeatPackage heartbeatPackage;

    public ScheduleRequestBody(Context context, long delayed, long period, TimeUnit timeUnit) {
        this.delayed = delayed;
        mTimeUnit = timeUnit;
        this.period = period;
        heartbeatPackage = new HeartbeatPackage(context);
        this.body = heartbeatPackage.get();
    }

    public void refreshBody(){
        this.body = heartbeatPackage.get();
    }


    @Override
    public byte[] getBody() {
        return body;
    }

    public long getDelayed() {
        return delayed;
    }

    public void setDelayed(long delayed) {
        this.delayed = delayed;
    }

    public TimeUnit getTimeUnit() {
        return mTimeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        mTimeUnit = timeUnit;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
