package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/19/2015.
 */
public abstract class GeoSlidingPanelBase {

    protected  SlidingUpPanelLayout mPanel;
    protected Context mContext;

    public GeoSlidingPanelBase(SlidingUpPanelLayout panel){
        mPanel = panel;
        mContext = panel.getContext();
    }

    public void setup(){
        setDefaultCollapsedUI();

        mPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
                setDefaultCollapsedUI();
            }

            @Override
            public void onPanelExpanded(View view) {
                expandedBehaviour();
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    protected void expandedBehaviour() {

    }

    protected abstract void setDefaultCollapsedUI();

    protected void completeDefaultCollapsedUI(Fragment collapsedFragment){
        Bundle args = getArguments();

        collapsedFragment.setArguments(args);

        FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.slider_content, collapsedFragment)
                .commit();
    }

    public void anchor() {
        anchorPanel(0.7f);
    }

    public SlidingUpPanelLayout.PanelState getPanelState() {
        return mPanel.getPanelState();
    }

    public void collapse() {
        if(getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mPanel.setTouchEnabled(true);
        }
    }

    public void expand(){
        if(getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            mPanel.setTouchEnabled(false);
        }
    }

    public void hide() {
        mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    public void touch(Boolean enabled) {
        mPanel.setTouchEnabled(enabled);
    }

    protected Bundle getArguments() {
        return (((MainActivity)mContext).getContentFragment()).getArguments();
    }

    public void halfAnchor() {
        anchorPanel(0.5f);
    }

    protected void anchorPanel(float anchorPoint) {
        if (getPanelState() != SlidingUpPanelLayout.PanelState.ANCHORED) {
            mPanel.setAnchorPoint(anchorPoint);
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }
    }
}
