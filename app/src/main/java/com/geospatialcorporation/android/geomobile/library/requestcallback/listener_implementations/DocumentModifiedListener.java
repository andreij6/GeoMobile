package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;

import retrofit.client.Response;

public class DocumentModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {
    private static final String TAG = DocumentModifiedListener.class.getSimpleName();

    public DocumentModifiedListener(){ super(true);}
    public DocumentModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
            try {
                if (mContentFragment instanceof IContentRefresher) {
                    ((IContentRefresher) mContentFragment).refresh();
                }
            } catch (Exception e){
                Log.d(TAG, e.getMessage()); //cannot cast content fragment to libraryfragment when adding doc to feature window
            }
        }
    }
}
