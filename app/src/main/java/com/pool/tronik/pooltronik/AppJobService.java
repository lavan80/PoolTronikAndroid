package com.pool.tronik.pooltronik;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.pool.tronik.pooltronik.utils.IService;

public class AppJobService extends JobService implements IService {
    public AppJobService() {
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d("UJUJHY","onStartJob");
        TokenHelper tokenHelper = new TokenHelper(jobParameters, AppJobService.this);
        /*PersistableBundle persistableBundle = jobParameters.getExtras();
        if (persistableBundle != null) {
            int key = persistableBundle.getInt(StaticVarFile.JOB_KEY, StaticVarFile.GET_TOKEN);
            Log.d("UJUJHY","onStartJob key="+key);
            if (key == StaticVarFile.GET_TOKEN)
                tokenHelper.askToken(getApplicationContext());
            else
                tokenHelper.sendToken(getApplicationContext(), FileUtil.getFcmToken(getApplicationContext()));
        }
        else*/
           tokenHelper.askToken(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("UJUJHY","onStopJob");
        return true;
    }


    @Override
    public void finished(JobParameters jobParameters, boolean isReschedule) {
        Log.d("UJUJHY","isReschedule="+isReschedule);
        jobFinished(jobParameters, isReschedule);
    }

    @Override
    public void onDestroy() {
        Log.d("UJUJHY","onDestroy");
        super.onDestroy();
    }
}
