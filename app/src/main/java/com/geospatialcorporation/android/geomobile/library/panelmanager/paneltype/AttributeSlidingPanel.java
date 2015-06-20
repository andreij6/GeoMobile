package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
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

        completeDefaultCollapsedUI(collapsedFragment);
    }


}
