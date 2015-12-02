package com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

public class PluginOwnerClientSelectorFragment extends ClientSelectorFragmentBase {
    private static final int CURSOR_LOADER_ID = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientTypeCode = ClientTypeCodes.PLUGINOWNERS.getKey();
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }
}
