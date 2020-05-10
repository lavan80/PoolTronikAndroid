package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pool.tronik.pooltronik.utils.RelayStatus;

import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ControllerNetRequest extends AbstractRequest{

    private RelayStatus relayStatus;

    public ControllerNetRequest(LifecycleOwner lifecycleOwner, Observer observer,RelayStatus relayStatus){
        super(lifecycleOwner, observer);
        this.relayStatus = relayStatus;
    }

    public void call() {
        if (NetConfig.BASE_CONTROLLER_URL.equals(NetConfig.IP_PREFIX))
            return;
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_CONTROLLER_URL
                , ScalarsConverterFactory.create())
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.getData(relayStatus.getAvailableCommand()).enqueue(new NetCallback<>());
    }
}
