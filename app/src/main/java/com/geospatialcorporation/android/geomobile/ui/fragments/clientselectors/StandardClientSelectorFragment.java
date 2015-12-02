package com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

public class StandardClientSelectorFragment extends ClientSelectorFragmentBase {

    private static final int CURSOR_LOADER_ID = 0;
    private static final String LOG_TAG = StandardClientSelectorFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientTypeCode = ClientTypeCodes.STANDARD.getKey();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        super.onActivityCreated(savedInstanceState);
    }


}
