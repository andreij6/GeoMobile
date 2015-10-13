package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.support.annotation.NonNull;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

        application.setLayerFolders(new ArrayList<Folder>());

        if(mShouldRefresh) {
           // ((LayerFragment) mContentFragment).refresh();
            if(mContentFragment instanceof IContentRefresher){
                ((IContentRefresher) mContentFragment).refresh();
            }

            if(mContentFragment instanceof LayerFragment) {

                LayerSelectorDrawerFragment drawerFragment = application.getMainActivity().getLayerDrawerFragment();

                drawerFragment.refresh();
            }
        }
    }
}
