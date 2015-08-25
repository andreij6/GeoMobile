package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import retrofit.client.Response;

public class AttributeValueModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {

    public AttributeValueModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    public AttributeValueModifiedListener(){ super(true);}

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh){
            ((GoogleMapFragment)mContentFragment).refreshFeatureWindow(1);
        }
    }
}
