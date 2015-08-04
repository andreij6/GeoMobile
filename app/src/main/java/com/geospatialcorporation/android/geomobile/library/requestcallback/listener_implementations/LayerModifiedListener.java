package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
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

            LayerSelectorDrawerFragment drawerFragment = getMainActivity().getLayerDrawerFragment();

            drawerFragment.refresh();
        }
    }
}
