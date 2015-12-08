package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.MapDefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MapSlidingPanel extends GeoSlidingPanelBase {

    GoogleMapFragment mMapFragment;

    public MapSlidingPanel(SlidingUpPanelLayout panel){
        super(panel);
        mMapFragment = (GoogleMapFragment)((MainActivity)mPanel.getContext()).getContentFragment();
    }

    @Override
    protected void setDefaultCollapsedUI() {
        Fragment collapsedFragment = new MapDefaultCollapsedPanelFragment();

        completeDefaultCollapsedUI(collapsedFragment);

        mMapFragment.clearHighlights();
    }

}
