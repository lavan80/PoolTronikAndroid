package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetStateRelayRequest extends AbstractRequest{
    public GetStateRelayRequest(LifecycleOwner lifecycleOwner, Observer observer) {
        super(lifecycleOwner, observer);
    }

    @Override
    public void call() {
        if (NetConfig.BASE_CONTROLLER_URL.equals(NetConfig.IP_PREFIX)) {
            emitThrowable("");
            return;
        }
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_CONTROLLER_URL
                , ScalarsConverterFactory.create())
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.getRelayState().enqueue(new NetCallback<>());
    }
}
