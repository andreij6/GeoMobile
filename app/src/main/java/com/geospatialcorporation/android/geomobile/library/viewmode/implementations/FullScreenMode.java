package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class FullScreenMode implements IViewMode {
    Builder mBuilder;

    public FullScreenMode(Builder builder){
        mBuilder = builder;
    }

    @Override
    public void Disable(Boolean showPanel) {
        mBuilder.mActionBar.show();

        mBuilder.mFab.setVisibility(View.GONE);

        View mainDrawer = mBuilder.mDrawerLayout.findViewById(R.id.navigation_drawer);
        View layerDrawer = mBuilder.mDrawerLayout.findViewById(R.id.layer_drawer);

        ViewGroup.LayoutParams params1 = mainDrawer.getLayoutParams();
        ViewGroup.LayoutParams params2 = layerDrawer.getLayoutParams();

        ((ViewGroup.MarginLayoutParams)params1).setMargins(0, mBuilder.mMarginTop, 0, 0);
        ((ViewGroup.MarginLayoutParams)params2).setMargins(0, mBuilder.mMarginTop,0,0);

        mainDrawer.setLayoutParams(params1);
        layerDrawer.setLayoutParams(params2);

        mBuilder.mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public boolean isSame(IViewMode mode) {
        return mode instanceof FullScreenMode;
    }

    public static class Builder {

        ActionBar mActionBar;
        DrawerLayout mDrawerLayout;
        SlidingUpPanelLayout mSlidingUpPanelLayout;
        FloatingActionButton mFab;
        int mMarginTop;

        public FullScreenMode create(ActionBar actionBar, DrawerLayout drawerLayout, SlidingUpPanelLayout panel, FloatingActionButton fab){
            mActionBar = actionBar;
            mDrawerLayout = drawerLayout;
            mSlidingUpPanelLayout = panel;
            mFab = fab;

            mFab.setVisibility(View.VISIBLE);

            mActionBar.hide();

            View mainDrawer = mDrawerLayout.findViewById(R.id.navigation_drawer);
            View layerDrawer = mDrawerLayout.findViewById(R.id.layer_drawer);

            ViewGroup.LayoutParams params1 = mainDrawer.getLayoutParams();
            ViewGroup.LayoutParams params2 = layerDrawer.getLayoutParams();

            mMarginTop = ((ViewGroup.MarginLayoutParams)params1).topMargin;

            ((ViewGroup.MarginLayoutParams)params1).setMargins(0, 0, 0, 0);
            ((ViewGroup.MarginLayoutParams)params2).setMargins(0,0,0,0);

            mainDrawer.setLayoutParams(params1);
            layerDrawer.setLayoutParams(params2);

            return new FullScreenMode(this);
        }

    }
}
