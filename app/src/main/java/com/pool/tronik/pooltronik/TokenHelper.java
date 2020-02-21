package com.pool.tronik.pooltronik;

import android.app.job.JobParameters;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pool.tronik.pooltronik.dto.PushEntity;
import com.pool.tronik.pooltronik.net.NetConfig;
import com.pool.tronik.pooltronik.net.ServerNetRequest;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.IService;
import com.pool.tronik.pooltronik.utils.ITokenCallBack;
import com.pool.tronik.pooltronik.utils.NotificationHelper;
import com.pool.tronik.pooltronik.utils.StaticVarFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenHelper {

    private final int MAX_REPETITION = 2;
    private JobParameters jobParameters;
    private IService iService;
    private ITokenCallBack iTokenCallBack;

    public TokenHelper(JobParameters jobParameters, IService iService) {
        this.jobParameters = jobParameters;
        this.iService = iService;
    }
    public TokenHelper(ITokenCallBack iTokenCallBack) {
        this.iTokenCallBack = iTokenCallBack;
    }
    public TokenHelper() {

    }

    public void askToken(final Context context) {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        if (FileUtil.isHasNewToken(context)) {
            Log.d("UJUJHY", "askToken HasNewToken");
            sendToken(context, FileUtil.getFcmToken(context));
        }
        else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.d("UJUJHY", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            sendToken(context, token);
                            Log.d("UJUJHY", "token=" + token);
                        }
                    });
        }
    }

    public void sendToken(final Context context, final String token){
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        PushEntity pushEntity = new PushEntity();
        pushEntity.setToken(token);
        pushEntity.setUniqId(deviceId);
        pushEntity.setPlatform(StaticVarFile.PLATFORM_ANDROID);
        ServerNetRequest serverNetRequest = new ServerNetRequest(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("UJUJHY","onResponse");
                FileUtil.setFcmToken(context, token);
                FileUtil.setIsHasNewToken(context, false);
                FileUtil.setIsHaveToUpdateToken(context, false);
                //FileUtil.setRepetitionCounter(context,0);
                if (iTokenCallBack != null)
                    iTokenCallBack.success(response);
                /*if (isScheduled()) {
                    JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    iService.finished(jobParameters,false);
                    scheduler.cancel(jobParameters.getJobId());
                }*/
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("UJUJHY","sendToken onFailure="+t.getMessage());
                if (iTokenCallBack != null)
                    iTokenCallBack.error(t);
                NotificationHelper.showSystemNotification(context.getApplicationContext()
                        ,context.getResources().getString(R.string.user_attention)
                        ,R.drawable.warning_icon,ActivityCommonSettings.class);
                FileUtil.setIsHaveToUpdateToken(context, true);
                /*Log.d("UJUJHY","isScheduled()="+isScheduled()+"; isHasMoreRepetition(context)="+isHasMoreRepetition(context));
                if (!isScheduled() && isHasMoreRepetition(context)) {
                    Log.d("UJUJHY","isScheduled() && isHasMoreRepetition(context");
                    JobUtil.scheduleJob(context);
                }
                else
                    iService.finished(jobParameters,isHasMoreRepetition(context));*/
            }
        }, pushEntity);
        serverNetRequest.call();
    }

    public static boolean isNeedRefresh(Context context) {
        String token = FileUtil.getFcmToken(context);
        if (token.isEmpty() || FileUtil.isHasNewToken(context))
            return true;
        return false;
    }
    public boolean isScheduled() {
        return jobParameters != null?true:false;
    }

    public boolean isHasMoreRepetition(Context context) {
        return FileUtil.getRepetitionCounter(context) < MAX_REPETITION;
    }

    /*JobScheduler js = context.getSystemService(JobScheduler.class);
        List<JobInfo> jobs = js.getAllPendingJobs();
        if (jobs == null) {
            return false;
        }
        for (int i=0; i<jobs.size(); i++) {
            if (jobs.get(i).getId() == JobIds.PHOTOS_CONTENT_JOB) {
                return true;
            }
        }
        return false;*/

     /*Log.d("UJUJHY", "FileUtil.getRepetitionCounter(context)="+FileUtil.getRepetitionCounter(context));
        if (!isHasMoreRepetition(context)) {
            if (isScheduled()) {
                Log.d("UJUJHY", "no more repetition");
                JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                iService.finished(jobParameters,false);
                scheduler.cancel(jobParameters.getJobId());
            }
            MyFirebaseMessagingService.showSystemNotification(context,"Please update your" +
                    " notification token",ActivityCommonSettings.class);
            FileUtil.setRepetitionCounter(context,0);
            return;
        }
        else {
            Log.d("UJUJHY", "repetition++");
            FileUtil.setRepetitionCounter(context,FileUtil.getRepetitionCounter(context)+1);
        }*/

}
