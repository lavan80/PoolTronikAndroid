package com.pool.tronik.pooltronik.net;

import com.pool.tronik.pooltronik.dto.PTScheduleDate;
import com.pool.tronik.pooltronik.dto.PushEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WebRelayRetrofitService {
    @GET()
    Call<String> getData(@Url String url);

    @POST("refresh")
    Call<Boolean> postToken(@Body PushEntity pushEntity);

    @POST("schedule")
    Call<Boolean> postTask(@Body PTScheduleDate ptScheduleDate);

    @GET("tasks")
    Call<List<PTScheduleDate>> getTasks(@Query("relay") String relay);
}
