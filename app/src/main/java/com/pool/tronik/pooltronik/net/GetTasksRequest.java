package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.pool.tronik.pooltronik.dto.PTScheduleDate;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetTasksRequest extends AbstractRequest{
    private int relay;

    public GetTasksRequest(LifecycleOwner lifecycleOwner, Observer observer, int relay) {
        super(lifecycleOwner,observer);
        this.relay = relay;

    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
            emitThrowable("");
            return;
        }
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_SERVER_URL)
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.getTasks(""+relay).enqueue(new NetCallback<>());
    }
}
