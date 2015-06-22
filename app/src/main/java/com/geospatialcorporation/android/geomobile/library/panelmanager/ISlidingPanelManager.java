package com.geospatialcorporation.android.geomobile.library.panelmanager;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/7/2015.
 */
public interface ISlidingPanelManager {

    void setup();

    void anchor();

    void collapse();

    void hide();

    void expand();

    void touch(Boolean enabled);

    SlidingUpPanelLayout.PanelState getPanelState();

    void toggle();
}
