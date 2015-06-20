package com.geospatialcorporation.android.geomobile.library.requestcallback;

import retrofit.RetrofitError;

/**
 * Created by andre on 6/20/2015.
 */
public interface RequestListener<T> {
    void onSuccess(T response);

    void onFailure(RetrofitError error);
}
