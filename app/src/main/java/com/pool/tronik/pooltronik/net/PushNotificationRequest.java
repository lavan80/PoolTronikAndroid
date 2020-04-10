package com.pool.tronik.pooltronik.net;

import com.pool.tronik.pooltronik.dto.PushEntity;

import retrofit2.Callback;

/**
 * This class doesn't extend AbsRequest because it must to update token without LifeCycle owner
 */
public class PushNotificationRequest{
    private Callback callback;
    private PushEntity pushEntity;

    public PushNotificationRequest(Callback callback, PushEntity pushEntity){
        this.callback = callback;
        this.pushEntity = pushEntity;
    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX))
            return;
        RestClient restClient = RestClient.getInstance();
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_SERVER_URL)
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.postToken(pushEntity).enqueue(callback);
    }
}
