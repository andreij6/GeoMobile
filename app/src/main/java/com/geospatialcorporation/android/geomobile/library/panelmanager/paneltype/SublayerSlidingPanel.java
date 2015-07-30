package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.SublayerDefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/19/2015.
 */
public class SublayerSlidingPanel extends GeoSlidingPanelBase {

    Layer mSublayer;

    public SublayerSlidingPanel(SlidingUpPanelLayout panel) {
        super(panel);
    }

    @Override
    protected void setDefaultCollapsedUI() {
        Fragment collapsedFragment = new SublayerDefaultCollapsedPanelFragment();

        //completeDefaultCollapsedUI(collapsedFragment);
    }
}
