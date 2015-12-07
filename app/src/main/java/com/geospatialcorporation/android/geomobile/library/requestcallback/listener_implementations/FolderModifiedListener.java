package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;

import java.util.ArrayList;

import retrofit.client.Response;

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

        application.setLayerFolders(new ArrayList<Folder>());

        if(mShouldRefresh) {
            if(mContentFragment instanceof LayerFragment){
                LayerSelectorDrawerFragment drawerFragment = application.getMainActivity().getLayerDrawerFragment();

                drawerFragment.refresh();
            }

            if (mContentFragment instanceof IContentRefresher){
                ((IContentRefresher) mContentFragment).refresh();
            }


        }
    }
}
