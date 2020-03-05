package com.pool.tronik.pooltronik.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteTaskRequest {
    private Callback callback;
    private int id;

    public DeleteTaskRequest(Callback callback, int id) {
        this.callback = callback;
        this.id = id;
    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        //ScalarsConverterFactory GsonConverterFactory
        //NetConfig.BASE_SERVER_URL is not empty!!!!
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.BASE_SERVER_URL)//"http://10.0.1.36:8080/"
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();
        WebRelayRetrofitService webRelayRetrofitService = retrofit.create(WebRelayRetrofitService.class);
        webRelayRetrofitService.removeTask(""+id).enqueue(callback);
    }
}
