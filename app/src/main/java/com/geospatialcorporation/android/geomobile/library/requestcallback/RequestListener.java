package com.geospatialcorporation.android.geomobile.library.requestcallback;

import retrofit.RetrofitError;


public interface RequestListener<T> {
    void onSuccess(T response);

    void onFailure(RetrofitError error);
}
