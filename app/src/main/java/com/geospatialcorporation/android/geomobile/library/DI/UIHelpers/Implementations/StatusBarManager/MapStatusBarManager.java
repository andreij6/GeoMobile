package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

public class MapStatusBarManager extends StatusBarManagerBase {

    private static final String TAG = MapStatusBarManager.class.getSimpleName();

    protected IMapStatusCtrl getMapStatusCtrl() {
        try {
            return (GoogleMapFragment) application.getMainActivity().getContentFragment();
        } catch (NullPointerException n){
            return null;
        }
    }
}
