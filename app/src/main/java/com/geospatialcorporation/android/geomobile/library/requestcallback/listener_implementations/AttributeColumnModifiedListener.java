package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;

import retrofit.client.Response;

/**
 * Created by andre on 6/22/2015.
 */
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
