package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pool.tronik.pooltronik.dto.ControllerEntity;

public class SetControllerIpRequest extends AbstractRequest {

    private ControllerEntity controllerEntity;

    public SetControllerIpRequest(LifecycleOwner lifecycleOwner, Observer observer, ControllerEntity controllerEntity) {
        super(lifecycleOwner, observer);
        this.controllerEntity = controllerEntity;
    }

    @Override
    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
            return;
        }
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_SERVER_URL)
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.setControllerIp(controllerEntity).enqueue(new NetCallback<>());
    }
}
