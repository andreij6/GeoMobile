package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;

import retrofit.client.Response;

/**
 * Created by andre on 6/22/2015.
 */
public class AttributeColumnModifiedListener extends RequestListenerBase implements RequestListener<Response> {
    public AttributeColumnModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    public AttributeColumnModifiedListener(){super(true);}

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh){
            Toaster("Should Refresh this Fragment");
        }
    }
}
