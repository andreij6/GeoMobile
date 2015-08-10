package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;

import retrofit.client.Response;

/**
 * Created by andre on 6/20/2015.
 */
public class FolderModifiedListener extends RequestListenerBase<Response> implements RequestListener<Response> {

    public FolderModifiedListener(){
        super(true);
    }
    public FolderModifiedListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    @Override
    public void onSuccess(Response response) {
        super.onSuccess(response);

        if(mShouldRefresh) {
            if(mContentFragment instanceof LayerFragment){
                ((LayerFragment) mContentFragment).refresh();

                LayerSelectorDrawerFragment drawerFragment = getMainActivity().getLayerDrawerFragment();

                drawerFragment.refresh();

            } else if (mContentFragment instanceof LibraryFragment){
                ((LibraryFragment) mContentFragment).refresh();
            }


        }
    }
}
