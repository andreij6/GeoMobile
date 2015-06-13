package com.geospatialcorporation.android.geomobile.library.helpers.panelmanager;

import android.view.Menu;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/7/2015.
 */
public interface ISlidingPanelManager {

    void anchor();

    void setup();

    void collapse();

    void hide();

    void touch(Boolean enabled);

    SlidingUpPanelLayout.PanelState getPanelState();
}
