package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.BookmarkFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.QuickSearchFragment;
import com.google.android.gms.maps.GoogleMap;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkMode implements IViewMode {

    Builder mBuilder;

    public BookmarkMode(Builder builder){
        mBuilder = builder;
    }

    @Override
    public void Disable(Boolean showPanel) {
        mBuilder.Reset(showPanel);
    }

    @Override
    public boolean isSame(IViewMode mode) {
        return mode instanceof BookmarkMode;
    }

    public static class Builder {

        FloatingActionButton mSave;
        FloatingActionButton mClose;
        SlidingUpPanelLayout mPanel;
        GoogleMap mMap;

       public Builder init(FloatingActionButton add, FloatingActionButton close, SlidingUpPanelLayout panel, final GoogleMap map, FragmentManager fm){
            mSave = add;
           mClose = close;
           mPanel = panel;
           mMap = map;

           add.setVisibility(View.VISIBLE);
           close.setVisibility(View.VISIBLE);
           mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

           SetClickEvents(add, close, fm);

           return this;
       }

        protected void SetClickEvents(FloatingActionButton add, FloatingActionButton close, final FragmentManager fm) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPanel.setTouchEnabled(false);

                    Fragment f = new BookmarkFragment.Builder(mMap, mPanel).create();

                    fm.beginTransaction()
                            .replace(R.id.slider_content, f)
                            .commit();

                    anchorSlider();

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reset(true);
                }
            });
        }

        protected void Reset(Boolean showPanel){
            mSave.setVisibility(View.GONE);
            mClose.setVisibility(View.GONE);
            if(showPanel) {
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }

        public BookmarkMode create(){
            return new BookmarkMode(this);
        }

        //region Slider Helpers
        protected void anchorSlider(){
            mPanel.setAnchorPoint(0.7f);
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }

        //endregion
    }
}
