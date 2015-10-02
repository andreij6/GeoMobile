package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.app.Activity;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

public abstract class TabletPanelFragmentBase extends TabGeoViewFragmentBase {

    private static final String TAG = TabletPanelFragmentBase.class.getSimpleName();

    protected IPanelFragmentCtrl mPanelCtrl;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            if (DeviceTypeUtil.isTablet(activity.getResources())) {
                mPanelCtrl = ((IPanelFragmentCtrl) ((MainTabletActivity) activity).getContentFragment());
            } else {
                //TODO:
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
