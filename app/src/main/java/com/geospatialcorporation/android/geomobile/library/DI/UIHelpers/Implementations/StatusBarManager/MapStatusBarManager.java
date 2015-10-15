package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

public class MapStatusBarManager extends StatusBarManagerBase {

    private static final String TAG = MapStatusBarManager.class.getSimpleName();

    protected IMapStatusCtrl getMapStatusCtrl() {
        if(application.getIsTablet()){
            return ((MainTabletActivity)(application.getGeoMainActivity())).getMapFragment();
        }

        return (GoogleMapFragment)application.getMainActivity().getContentFragment();
    }
}
