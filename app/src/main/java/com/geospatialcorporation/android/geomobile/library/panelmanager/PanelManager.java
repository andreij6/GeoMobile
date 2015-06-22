package com.geospatialcorporation.android.geomobile.library.panelmanager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.AttributeSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.GeoSlidingPanelBase;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.MapSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.SublayerSlidingPanel;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/19/2015.
 */
public class PanelManager implements ISlidingPanelManager {

    GeoSlidingPanelBase mGeoPanel;

    public PanelManager(int panelType){
        SlidingUpPanelLayout panel = application.getSlidingPanel(panelType);

        switch (panelType){
            case GeoPanel.MAP:
                mGeoPanel = new MapSlidingPanel(panel);
                break;
            case GeoPanel.SUBLAYER:
                mGeoPanel = new SublayerSlidingPanel(panel);
                break;
            case GeoPanel.LAYER_ATTRIBUTE:
                mGeoPanel = new AttributeSlidingPanel(panel);
                break;
            default:
                break;
        }
    }

    @Override
    public void setup() {
        mGeoPanel.setup();
    }

    @Override
    public void anchor() {
        mGeoPanel.anchor();
    }

    @Override
    public void collapse() {
        mGeoPanel.collapse();
    }

    @Override
    public void hide() {
        mGeoPanel.hide();
    }

    @Override
    public void expand() {
        mGeoPanel.expand();
    }

    @Override
    public void touch(Boolean enabled) {
        mGeoPanel.touch(enabled);
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return mGeoPanel.getPanelState();
    }

    @Override
    public void toggle() {
        if(mGeoPanel.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            collapse();
        } else {
            anchor();
        }
    }
}
