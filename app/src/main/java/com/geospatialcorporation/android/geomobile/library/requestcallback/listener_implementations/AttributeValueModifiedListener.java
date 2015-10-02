package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.TabletFeatureWindowPanelFragment;

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
            if(application.getIsTablet()) {
                TabletFeatureWindowPanelFragment contentFrag = (TabletFeatureWindowPanelFragment) application.getGeoMainActivity().getContentFragment();

                contentFrag.refreshFeatureWindow(1);
            } else {
                ((GoogleMapFragment) mContentFragment).refreshFeatureWindow(1);
            }
        }
    }
}
