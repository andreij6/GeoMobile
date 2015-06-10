package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
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
        GoogleMap mMap;
        SlidingPanelManager mPanelManager;

       public Builder init(FloatingActionButton add, FloatingActionButton close, SlidingPanelManager panelManager, final GoogleMap map, FragmentManager fm){
            mSave = add;
           mClose = close;
           mMap = map;
           mPanelManager = panelManager;

           add.setVisibility(View.VISIBLE);
           close.setVisibility(View.VISIBLE);
           mPanelManager.hide();

           SetClickEvents(add, close, fm);

           return this;
       }

        protected void SetClickEvents(FloatingActionButton add, FloatingActionButton close, final FragmentManager fm) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPanelManager.touch(false);

                    Fragment f = new BookmarkFragment.Builder(mMap).create();

                    fm.beginTransaction()
                            .replace(R.id.slider_content, f)
                            .commit();

                    mPanelManager.anchor();

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
                mPanelManager.collapse();
            }
        }

        public BookmarkMode create(){
            return new BookmarkMode(this);
        }

    }
}
