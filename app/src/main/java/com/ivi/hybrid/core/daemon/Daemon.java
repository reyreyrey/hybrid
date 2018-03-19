package com.ivi.hybrid.core.daemon;

import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.ivi.hybrid.core.log.Log;
import com.ivi.hybrid.core.push.Push;
import com.ivi.hybrid.utils.LogUtil;

import java.util.Set;

/**
 * author: Rea.X
 * date: 2017/11/15.
 */

public class Daemon extends Job {
    public static final String TAG = "daemon job";
    public static final String CHANNEL_ID = "daemon_channel_id";
    public static final String CHANNEL_NAME = "daemon_channel_name";
    public static final int NOTICE_ID = 110;
    private Application application;

    public Daemon(Application application) {
        this.application = application;
    }

    public static void schedule() {
        Set<Job> jobs = JobManager.instance().getAllJobsForTag(Daemon.TAG);
        if (jobs != null && jobs.size() >= 10) {
            return;
        }
        if (jobs.isEmpty()) {
            new JobRequest.Builder(Daemon.TAG)
                    .setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                    .setUpdateCurrent(true)
                    .build()
                    .schedule();
        }
    }

    public static void daemonPush(Context context) {
        Push.getInstance().start(context);
        Log.init(context);
    }

    public static void compatibleO2(Service service) {
//        final NotificationManager manager = (NotificationManager) service.getSystemService(service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);
//            manager.createNotificationChannel(channel);
            service.startForeground(1, new Notification.Builder(service, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("")
                    .setAutoCancel(true)
                    .build()
            );
        } else {
//            service.startForeground(1, new NotificationCompat.Builder(service, null)
//                    .setContentTitle("")
//                    .setContentText("")
//                    .setPriority(NotificationCompat.PRIORITY_MIN)
//                    .setAutoCancel(true).build()
//            );
        }
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        daemonPush(application);
        return Result.SUCCESS;
    }
}
