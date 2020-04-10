package com.pool.tronik.pooltronik.net;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AbstractRequest {

    protected MutableLiveData<Object> mutableLiveData;
    protected LifecycleOwner lifecycleOwner;
    protected Observer observer;
    protected RestClient restClient;

    public AbstractRequest(LifecycleOwner lifecycleOwner, Observer observer) {
        mutableLiveData = new MutableLiveData<>();
        this.lifecycleOwner = lifecycleOwner;
        this.observer = observer;
        restClient = RestClient.getInstance();
        subscribe();
    }

    public abstract void call();

    private void subscribe() {
        mutableLiveData.observe(lifecycleOwner, observer);
    }

    public void emit(Object o) {
        mutableLiveData.setValue(o);
    }

    protected class NetCallback<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            emit(response);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            emit(t);
        }
    }
}
