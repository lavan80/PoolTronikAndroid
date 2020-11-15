package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pool.tronik.pooltronik.dto.PTScheduleDate;

public class TaskNetRequest extends AbstractRequest{

    private PTScheduleDate ptScheduleDate;

    public TaskNetRequest(LifecycleOwner lifecycleOwner, Observer observer, PTScheduleDate ptScheduleDate) {
        super(lifecycleOwner, observer);
        this.ptScheduleDate = ptScheduleDate;
    }

    public void call() {
        if (NetConfig.BASE_SERVER_URL.equals(NetConfig.IP_PREFIX)) {
            emitThrowable("");
            return;
        }

        WebRelayRetrofitService webRelayRetrofitService = restClient.getRetrofit(NetConfig.BASE_SERVER_URL)
                .create(WebRelayRetrofitService.class);
        webRelayRetrofitService.postTask(ptScheduleDate).enqueue(new NetCallback<>());
    }
}
