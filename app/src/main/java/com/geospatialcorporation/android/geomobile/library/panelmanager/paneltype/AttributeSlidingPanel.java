package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.AttributeDefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/19/2015.
 */
public class AttributeSlidingPanel extends GeoSlidingPanelBase {

    public AttributeSlidingPanel(SlidingUpPanelLayout panel) {
        super(panel);
    }

    @Override
    protected void setDefaultCollapsedUI() {
        Fragment collapsedFragment = new AttributeDefaultCollapsedPanelFragment();

        //completeDefaultCollapsedUI(collapsedFragment);
    }

}
