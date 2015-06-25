package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.DocumentFragment;

import retrofit.client.Response;

/**
 * Created by andre on 6/20/2015.
 */
public class DocumentModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {

    public DocumentModifiedListener(){ super(true);}
    public DocumentModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
            ((DocumentFragment) mContentFragment).refresh();
        }
    }
}
