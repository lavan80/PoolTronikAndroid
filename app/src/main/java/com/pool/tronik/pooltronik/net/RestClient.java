package com.pool.tronik.pooltronik.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static RestClient restClient;

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RestClient() {
        initOkHttpClient();
    }

    /**
     * Not thread safe
     * @return
     */
    public static RestClient getInstance() {
        if (restClient == null) {
            restClient = new RestClient();
        }

        return restClient;
    }

    private void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private void initRetrofit(String baseUrl) {
        //ScalarsConverterFactory GsonConverterFactory
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();
    }

    //.addConverterFactory(ScalarsConverterFactory.create())

    private void initRetrofit(String baseUrl, Converter.Factory factory) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
    }

    public Retrofit getRetrofit(String baseUrl) {

        if (retrofit != null && retrofit.baseUrl().toString().contains(baseUrl))
            return retrofit;

        initRetrofit(baseUrl);
        return retrofit;
    }

    public Retrofit getRetrofit(String baseUrl, Converter.Factory factory) {

        if (retrofit != null && retrofit.baseUrl().toString().contains(baseUrl))
            return retrofit;

        initRetrofit(baseUrl, factory);
        return retrofit;
    }

}
