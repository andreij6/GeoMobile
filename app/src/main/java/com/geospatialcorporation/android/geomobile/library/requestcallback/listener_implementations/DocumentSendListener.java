package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;

/**
 * Created by andre on 6/20/2015.
 */
public class DocumentSendListener extends RequestListenerBase<Document> implements RequestListener<Document> {
    private static final String TAG = DocumentSendListener.class.getSimpleName();

    ISendFileCallback mCallback;

    public DocumentSendListener(){ super(true);}
    public DocumentSendListener(ISendFileCallback callback) {
        super(true);
        mCallback = callback;
    }

    @Override
    public void onSuccess(Document response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
            try {
                if (mContentFragment instanceof LibraryFragment) {
                    ((LibraryFragment) mContentFragment).refresh();
                }
            } catch (Exception e){
                Log.d(TAG, e.getMessage()); //cannot cast content fragment to libraryfragment when adding doc to feature window
            }
        }

        if(mCallback != null) {
            mCallback.invoke(response);
        }
    }
}
