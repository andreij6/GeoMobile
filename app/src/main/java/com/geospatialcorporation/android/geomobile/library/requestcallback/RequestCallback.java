package com.geospatialcorporation.android.geomobile.library.requestcallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/20/2015.
 */
public class RequestCallback<T> implements Callback<T> {

    protected RequestListener<T> listener;

    public RequestCallback(RequestListener<T> listener){
        this.listener = listener;
    }

    @Override
    public void success(T t, Response response) {
        listener.onSuccess(t);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onFailure(error);
    }
}
