package com.pool.tronik.pooltronik;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.pool.tronik.pooltronik.utils.NotificationHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("UJUJHY", "Refreshed token: " + token);
        FileUtil.setFcmToken(getApplicationContext(), token);
        FileUtil.setIsHasNewToken(getApplicationContext(), true);
        TokenHelper tokenHelper = new TokenHelper();
        tokenHelper.askToken(getApplicationContext());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try
        {
            /*Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            String s = object.getString("MESSAGE_KEY");*/
            String text = getString(R.string.alert_txt);
            Log.d("UJUJHY", "onMessageReceived: "+text);
            NotificationHelper.showSystemNotification(getApplicationContext(),text,R.drawable.alarm_white,AllertActivity.class);
        }catch (Exception e){
            Log.d("UJUJHY", "onMessageReceived Exception="+e.getMessage());
        }

    }

}
