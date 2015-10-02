package com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public interface ILayerDetailCommons {

    Layer handleArguments(Bundle args);

    ILayerDetailCommons panel(SlidingUpPanelLayout panel);

    void tabHost(FragmentTabHost tabHost, Resources resources, Bundle args);

    ISlidingPanelManager getPanelManager(SlidingUpPanelLayout panel);
}
