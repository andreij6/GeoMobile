package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.SublayersTab;

import retrofit.client.Response;

/**
 * Created by andre on 6/20/2015.
 */
public class SublayerModifiedListner extends RequestListenerBase<Response> implements RequestListener<Response> {

    public SublayerModifiedListner(){ super(true);}
    public SublayerModifiedListner(Boolean shouldRefresh){ super(shouldRefresh);}

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
            Fragment tab = ((LayerDetailFragment) mContentFragment).getCurrentTab();

            ((SublayersTab) tab).refresh();
        }
    }

}
