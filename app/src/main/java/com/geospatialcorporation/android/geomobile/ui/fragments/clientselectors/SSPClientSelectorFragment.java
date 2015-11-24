package com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

public class SSPClientSelectorFragment extends ClientSelectorFragmentBase {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientTypeCode = ClientTypeCodes.SSP.getKey();
    }
}
