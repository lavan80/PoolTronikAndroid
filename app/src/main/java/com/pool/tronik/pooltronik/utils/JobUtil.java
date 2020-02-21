package com.pool.tronik.pooltronik.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;

import com.pool.tronik.pooltronik.AppJobService;
import com.pool.tronik.pooltronik.net.NetConfig;

import java.util.concurrent.TimeUnit;

public class JobUtil {

    private static final int PERIOD = 20000;

    public static void scheduleJob(Context context) {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        ComponentName componentName = new ComponentName(context, AppJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(4756, componentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
             builder.setPeriodic(PERIOD,PERIOD);
        else
            builder.setPeriodic(PERIOD);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(PERIOD), JobInfo.BACKOFF_POLICY_LINEAR);
        /*PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt(StaticVarFile.JOB_KEY, step);
        builder.setExtras(persistableBundle);*/
        //builder.setPersisted(true); TEST IT
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int flag =jobScheduler.schedule(builder.build());
        Log.d("UJUJHY", "JobUtil flag="+flag);
    }
}
