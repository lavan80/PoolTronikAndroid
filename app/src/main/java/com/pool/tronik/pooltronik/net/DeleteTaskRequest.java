package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public class DeleteTaskRequest extends AbstractRequest{

    private int id;

    public DeleteTaskRequest(LifecycleOwner lifecycleOwner, Observer observer,int id) {
        super(lifecycleOwner,observer);
        this.id = id;
    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
            emitThrowable("");
            return;
        }
        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_SERVER_URL)
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.removeTask(""+id).enqueue(new NetCallback<>());
    }
}
