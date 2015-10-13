package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapStatusBarManager extends StatusBarManagerBase {

    @Override
    protected IMapStatusCtrl getMapStatusCtrl() {
        if(application.getIsTablet()){
            return ((MainTabletActivity)(application.getGeoMainActivity())).getMapFragment();
        }

        return (GoogleMapFragment)application.getMainActivity().getContentFragment();
    }
}
