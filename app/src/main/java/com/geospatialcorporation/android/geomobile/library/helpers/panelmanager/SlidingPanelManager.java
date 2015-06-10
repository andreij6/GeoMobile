package com.geospatialcorporation.android.geomobile.library.helpers.panelmanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.CollapsedPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/7/2015.
 */
public class SlidingPanelManager implements ISlidingPanelManager{
    SlidingUpPanelLayout mMapPanel;
    Context mContext;

    public SlidingPanelManager(Context context){
        mMapPanel = application.getMapFragmentPanel();
        mContext = context;
    }

    @Override
    public void anchor() {
        mMapPanel.setAnchorPoint(0.7f);
        mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    @Override
    public void setup(final Menu menu) {
        setDefaultCollapsedUI();

        mMapPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                setDefaultCollapsedUI();
                MenuItem item = menu.findItem(R.id.action_search);
                item.setVisible(true);
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
        mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mMapPanel.setTouchEnabled(true);
    }

    @Override
    public void hide() {
        mMapPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void touch(Boolean enabled) {
        mMapPanel.setTouchEnabled(enabled);
    }

    protected void setDefaultCollapsedUI(){
        Fragment collapsedFragment = new CollapsedPanelFragment();

        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.slider_content, collapsedFragment)
                .commit();
    }
}
