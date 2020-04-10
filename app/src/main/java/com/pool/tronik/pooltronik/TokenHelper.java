package com.pool.tronik.pooltronik;

import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pool.tronik.pooltronik.dto.PushEntity;
import com.pool.tronik.pooltronik.net.NetConfig;
import com.pool.tronik.pooltronik.net.PushNotificationRequest;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.ITokenCallBack;
import com.pool.tronik.pooltronik.utils.NotificationHelper;
import com.pool.tronik.pooltronik.utils.StaticVarFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenHelper {

    private final int MAX_REPETITION = 2;
    private ITokenCallBack iTokenCallBack;

    public TokenHelper(ITokenCallBack iTokenCallBack) {
        this.iTokenCallBack = iTokenCallBack;
    }
    public TokenHelper() {

    }

    public void askToken(final Context context) {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        if (FileUtil.isHasNewToken(context)) {
            sendToken(context, FileUtil.getFcmToken(context));
        }
        else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            sendToken(context, token);
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
        PushNotificationRequest serverNetRequest = new PushNotificationRequest(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                FileUtil.setFcmToken(context, token);
                FileUtil.setIsHasNewToken(context, false);
                FileUtil.setIsHaveToUpdateToken(context, false);
                if (iTokenCallBack != null)
                    iTokenCallBack.success(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (iTokenCallBack != null)
                    iTokenCallBack.error(t);
                NotificationHelper.showSystemNotification(context.getApplicationContext()
                        ,context.getResources().getString(R.string.user_attention)
                        ,R.drawable.warning_icon,ActivityCommonSettings.class);
                FileUtil.setIsHaveToUpdateToken(context, true);
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
}
