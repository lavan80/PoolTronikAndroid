package com.pool.tronik.pooltronik.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.pool.tronik.pooltronik.R;

public class NotificationHelper {

    public static void showSystemNotification(Context context, String notification, int idDrawable,Class aClass) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "notify_001")
                //.setDefaults(NotificationCompat.) https://stackoverflow.com/questions/51019645/setdefaults-is-deprecated-in-notification-builder
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker(context.getResources().getString(R.string.app_name))
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSmallIcon(idDrawable)
                .setContentTitle(notification)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, aClass), PendingIntent.FLAG_ONE_SHOT));
                /*Intent intent = new Intent(from, SplashActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION_MODEL, notification);
        return PendingIntent.getActivity(from, 0, intent, PendingIntent.FLAG_ONE_SHOT);*/
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001", context.getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationBuilder.setColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }
        //https://stackoverflow.com/questions/28387602/notification-bar-icon-turns-white-in-android-5-lollipop

        notificationManager.notify(0, notificationBuilder.build());
    }
}
