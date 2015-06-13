package com.geospatialcorporation.android.geomobile.library.helpers.panelmanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.DefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/7/2015.
 */
public class SlidingPanelManager implements ISlidingPanelManager{
    SlidingUpPanelLayout mMapPanel;
    Context mContext;
    GoogleMapFragment mMapFragment;

    public SlidingPanelManager(Context context){
        mMapPanel = application.getMapFragmentPanel();
        mContext = context;
        mMapFragment = (GoogleMapFragment)((MainActivity)context).getContentFragment();
    }

    @Override
    public void anchor() {
        if(getPanelState() != SlidingUpPanelLayout.PanelState.ANCHORED) {
            mMapPanel.setAnchorPoint(0.7f);
            mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }
    }

    @Override
    public void setup() {
        setDefaultCollapsedUI();


        mMapPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {}

            @Override
            public void onPanelCollapsed(View view) {
                setDefaultCollapsedUI();
            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

    }

    @Override
    public void collapse() {
        if(getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mMapPanel.setTouchEnabled(true);
            mMapFragment.resetViewMode();
        }
    }

    @Override
    public void hide() {
        mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void touch(Boolean enabled) {
        mMapPanel.setTouchEnabled(enabled);
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return mMapPanel.getPanelState();
    }

    protected void setDefaultCollapsedUI(){
        Fragment collapsedFragment = new DefaultCollapsedPanelFragment();

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.slider_content, collapsedFragment)
                .commit();
    }
}
