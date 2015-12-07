package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;

import retrofit.client.Response;

public class AttributeColumnModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {
    public AttributeColumnModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    public AttributeColumnModifiedListener(){super(true);}

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh){
            Toaster("Pull Down to Refresh Attributes");
        }
    }
}
