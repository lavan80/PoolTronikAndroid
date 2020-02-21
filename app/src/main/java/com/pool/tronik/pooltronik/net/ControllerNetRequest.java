package com.pool.tronik.pooltronik.net;

import com.pool.tronik.pooltronik.utils.RelayStatus;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ControllerNetRequest {

    private Callback callback;
    private RelayStatus relayStatus;

    public ControllerNetRequest(RelayStatus relayStatus, Callback callback){

        this.relayStatus = relayStatus;
        this.callback = callback;
    }

    public void call() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        //ScalarsConverterFactory GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.BASE_CONTROLLER_URL) //Базовая часть адреса
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WebRelayRetrofitService webRelayRetrofitService = retrofit.create(WebRelayRetrofitService.class);
        webRelayRetrofitService.getData(relayStatus.getAvailableCommand()).enqueue(callback);
    }
}
