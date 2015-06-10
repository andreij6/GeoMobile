package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.app.AlertDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/8/2015.
 */
public class FullScreenMode implements IViewMode {
    Builder mBuilder;

    public FullScreenMode(Builder builder){
        mBuilder = builder;
    }



    @Override
    public void Disable(Boolean showPanel) {
        mBuilder.mActionBar.show();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mBuilder.mDrawerLayout.getLayoutParams();

        params.setMargins(0, mBuilder.mMarginTop, 0, 0);

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
        int mMarginTop;

        public FullScreenMode create(ActionBar actionBar, DrawerLayout drawerLayout, SlidingUpPanelLayout panel){
            mActionBar = actionBar;
            mDrawerLayout = drawerLayout;
            mSlidingUpPanelLayout = panel;

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)drawerLayout.getLayoutParams();
            mMarginTop = params.topMargin;

            params.setMargins(0,0,0,0);

            mDrawerLayout.setLayoutParams(params);

            mActionBar.hide();
            return new FullScreenMode(this);
        }

    }
}
