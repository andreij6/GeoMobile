package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class SublayerSlidingPanel extends GeoSlidingPanelBase {

    Layer mSublayer;

    public SublayerSlidingPanel(SlidingUpPanelLayout panel) {
        super(panel);
    }

    @Override
    protected void setDefaultCollapsedUI() {
        //Fragment collapsedFragment = new SublayerDefaultCollapsedPanelFragment();

        //completeDefaultCollapsedUI(collapsedFragment);
    }
}
