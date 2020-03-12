package com.pool.tronik.pooltronik.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.pool.tronik.pooltronik.dto.PushEntity;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerNetRequest {
    private Callback callback;
    private PushEntity pushEntity;

    public ServerNetRequest(Callback callback, PushEntity pushEntity){
        this.callback = callback;
        this.pushEntity = pushEntity;
    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        //ScalarsConverterFactory GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();
        WebRelayRetrofitService webRelayRetrofitService = retrofit.create(WebRelayRetrofitService.class);
        webRelayRetrofitService.postToken(pushEntity).enqueue(callback);
    }
}
