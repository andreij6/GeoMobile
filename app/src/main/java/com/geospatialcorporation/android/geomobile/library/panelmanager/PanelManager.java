package com.geospatialcorporation.android.geomobile.library.panelmanager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.GeoSlidingPanelBase;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.LayerFragmentSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.LibraryFragmentSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.MapSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.SublayerSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.DocumentDetailSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.DocumentFolderSlidingPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype.LayerDetailSlidingPanel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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
            case GeoPanel.LAYER_FRAGMENT:
                mGeoPanel = new LayerFragmentSlidingPanel(panel);
                break;
            case GeoPanel.LIBRARY_FRAGMENT:
                mGeoPanel = new LibraryFragmentSlidingPanel(panel);
                break;
            case GeoPanel.DOCUMENT_DETAIL:
                mGeoPanel = new DocumentDetailSlidingPanel(panel);
                break;
            case GeoPanel.DOCUMENT_FOLDER_DETAIL:
                mGeoPanel = new DocumentFolderSlidingPanel(panel);
                break;
            case GeoPanel.LAYER_DETAIL:
                mGeoPanel = new LayerDetailSlidingPanel(panel);
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
        mGeoPanel.setIsOpen(false);
        mGeoPanel.collapse();
        mGeoPanel.touch(false);
    }

    @Override
    public void hide() {
        mGeoPanel.hide();
        mGeoPanel.setIsOpen(false);
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

    @Override
    public void halfAnchor() {
        mGeoPanel.setIsOpen(true);
        mGeoPanel.halfAnchor();
    }

    @Override
    public void setIsOpen(boolean open) {
        mGeoPanel.setIsOpen(open);
    }

    @Override
    public Boolean getIsOpen() {
        return mGeoPanel.getIsOpen();
    }

    @Override
    public void halfAnchor(float plus) {
        mGeoPanel.setIsOpen(true);
        mGeoPanel.halfAnchor(plus);
    }

    public boolean isExpanded() {
        return mGeoPanel.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public static class Builder {

        Boolean mHide;
        int mPanelType;

        public Builder(){
            mHide = false;
        }


        public Builder type(int panelType) {
            mPanelType = panelType;
            return this;
        }

        public Builder hide() {
            mHide = true;
            return this;
        }

        public ISlidingPanelManager build() {
            PanelManager manager = new PanelManager(mPanelType);
            manager.setup();

            if(mHide) {
                manager.hide();
            }
            return manager;
        }
    }
}
