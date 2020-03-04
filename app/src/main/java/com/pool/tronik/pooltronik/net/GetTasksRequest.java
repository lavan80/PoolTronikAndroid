package com.pool.tronik.pooltronik.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetTasksRequest {
    private Callback callback;
    private int relay;

    public GetTasksRequest(Callback callback, int relay) {
        this.callback = callback;
        this.relay = relay;

    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) //?????
            return;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();
        WebRelayRetrofitService webRelayRetrofitService = retrofit.create(WebRelayRetrofitService.class);
        webRelayRetrofitService.getTasks(""+relay).enqueue(callback);
    }
}
