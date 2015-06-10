package com.geospatialcorporation.android.geomobile.library.helpers.panelmanager;

import android.view.Menu;

/**
 * Created by andre on 6/7/2015.
 */
public interface ISlidingPanelManager {

    void anchor();

    void setup(Menu menu);

    void collapse();

    void hide();

    void touch(Boolean enabled);
}
