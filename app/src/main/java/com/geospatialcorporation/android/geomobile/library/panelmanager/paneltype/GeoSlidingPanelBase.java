package com.geospatialcorporation.android.geomobile.library.panelmanager.paneltype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelStateReactor;
import com.geospatialcorporation.android.geomobile.ui.activity.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DefaultCollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public abstract class GeoSlidingPanelBase {

    protected SlidingUpPanelLayout mPanel;
    protected Context mContext;
    protected Boolean mIsOpen;

    public Boolean getIsOpen(){
        return mIsOpen;
    }

    public void setIsOpen(Boolean open){
        mIsOpen = open;
    }

    public GeoSlidingPanelBase(SlidingUpPanelLayout panel){
        mPanel = panel;
        mContext = panel.getContext();
        mIsOpen = false;
    }

    public void setup(){
        //setDefaultCollapsedUI();
        hide();

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
                FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();

                Fragment sliderFragment = fragmentManager.findFragmentById(R.id.slider_content);

                if(sliderFragment != null) {
                    if (sliderFragment instanceof IPanelStateReactor) {
                        ((IPanelStateReactor) sliderFragment).Anchored();
                    }
                }
            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    protected void expandedBehaviour() {
        FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();

        Fragment sliderFragment = fragmentManager.findFragmentById(R.id.slider_content);

        if(sliderFragment instanceof IPanelStateReactor){
            ((IPanelStateReactor)sliderFragment).Expanded();
        }

    }

    protected void setDefaultCollapsedUI(){
        Fragment collapsedFragment = new DefaultCollapsedPanelFragment();

        completeDefaultCollapsedUI(collapsedFragment);
    }

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
        if(!getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
            mPanel.setPanelHeight(0);
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        }

        if(!getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
            mPanel.setPanelHeight(0);
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        }
    }

    public void touch(Boolean enabled) {
        mPanel.setTouchEnabled(enabled);
    }

    protected Bundle getArguments() {
        return (((MainActivity) mContext).getContentFragment()).getArguments();
    }

    public void halfAnchor() {
        anchorPanel(0.5f);
    }

    protected void anchorPanel(float anchorPoint) {
        mPanel.setAnchorPoint(anchorPoint);
        mPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    public void halfAnchor(float plus) {
        float half = 0.5f;

        anchorPanel(half + plus);
    }
}
