package com.pool.tronik.pooltronik.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pool.tronik.pooltronik.R;
import com.pool.tronik.pooltronik.net.NetConfig;

public class FileUtil {

    private static final String RELAY_STATUS = "relay_status";
    private static final String STATUS_FILE = "status_file";
    private static final String FILE_CONFIG = "file_config";
    private static final String FCM_TOKEN = "my_fcm_token";
    private static final String REPETITION_FILE = "my_repetition_file";

    public static boolean getRelayStatus(Context context, String relayOn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STATUS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(relayOn, false);
    }

    public static void setRelayStatus(Context context, String relayOn, boolean status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STATUS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(relayOn, status);
        editor.apply();
    }

    public static void resetParams(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STATUS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void setIp(Context context, String ip){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ip);
        editor.apply();
    }

    public static String getIp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        return sharedPreferences.getString("ip", NetConfig.BASE_CONTROLLER_URL);
    }

    public static void setServerIp(Context context, String ip){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("server_ip", ip);
        editor.apply();
    }

    public static String getServerIp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        return sharedPreferences.getString("server_ip", NetConfig.BASE_SERVER_URL);
    }

    public static void setRelayName(Context context, String relayOn, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(relayOn, name);
        editor.apply();
    }

    public static String getRelayName(Context context, String relayOn){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_CONFIG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(relayOn,context.getResources().getString(R.string.def_relay_name));
    }

    public static void setFcmToken(Context context,String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("my_token", token);
        editor.apply();
    }

    public static String getFcmToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_token", "");
    }

    public static boolean isHasNewToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("my_new token", false);
    }
    public static void setIsHasNewToken(Context context, boolean isHasNewToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("my_new token", isHasNewToken);
        editor.apply();
    }

    public static void setRepetitionCounter(Context context,int repetitionCounter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REPETITION_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("repetition_counter", repetitionCounter);
        editor.apply();
    }

    public static int getRepetitionCounter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REPETITION_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("repetition_counter", 0);
    }

    public static boolean isHaveToUpdateToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REPETITION_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_have_update_token", false);
    }

    public static void setIsHaveToUpdateToken(Context context, boolean isHaveToUpdate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REPETITION_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_have_update_token", isHaveToUpdate);
        editor.apply();
    }

}
