package com.geospatialcorporation.android.geomobile.library.panelmanager;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public interface ISlidingPanelManager {

    void setup();

    void anchor();

    void collapse();

    void hide();

    void expand();

    void touch(Boolean enabled);

    SlidingUpPanelLayout.PanelState getPanelState();

    void toggle();

    void halfAnchor();

    void setIsOpen(boolean open);

    Boolean getIsOpen();

    void halfAnchor(float plus);
}
