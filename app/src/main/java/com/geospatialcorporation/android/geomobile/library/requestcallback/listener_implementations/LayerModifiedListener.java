package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;

import retrofit.client.Response;


public class LayerModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {

    public LayerModifiedListener(boolean shouldRefresh){
        super(shouldRefresh);
    }

    public LayerModifiedListener(){
        super(true);
    }

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
           // ((LayerFragment) mContentFragment).refresh();
            if(mContentFragment instanceof IContentRefresher){
                ((IContentRefresher) mContentFragment).refresh();
            }

            if(!application.getIsTablet()) {

                LayerSelectorDrawerFragment drawerFragment = application.getMainActivity().getLayerDrawerFragment();

                drawerFragment.refresh();
            }
        }
    }
}
