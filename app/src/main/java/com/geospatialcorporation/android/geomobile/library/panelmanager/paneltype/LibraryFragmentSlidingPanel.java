package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class LibraryFragmentSlidingPanel extends GeoSlidingPanelBase {


    public LibraryFragmentSlidingPanel(SlidingUpPanelLayout panel) {
        super(panel);
    }

    @Override
    protected void setDefaultCollapsedUI() {
        Fragment collapsedFragment = new DefaultCollapsedPanelFragment();

        completeDefaultCollapsedUI(collapsedFragment);
    }
}
