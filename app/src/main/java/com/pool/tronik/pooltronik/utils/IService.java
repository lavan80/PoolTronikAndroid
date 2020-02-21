package com.pool.tronik.pooltronik.utils;

import android.app.job.JobParameters;

public interface IService {
    void finished(JobParameters jobParameters, boolean isReschedule);
}
