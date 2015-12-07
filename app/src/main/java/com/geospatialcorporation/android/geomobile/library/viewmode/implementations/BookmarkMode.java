package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.BookmarkPanelFragment;
import com.google.android.gms.maps.GoogleMap;
import com.melnykov.fab.FloatingActionButton;

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
        ISlidingPanelManager mPanelManager;

       public Builder init(FloatingActionButton add, FloatingActionButton close, ISlidingPanelManager panelManager, final GoogleMap map, FragmentManager fm){
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

                    Fragment f = new BookmarkPanelFragment.Builder(mMap).create();

                    //fm.beginTransaction()
                    //        .replace(R.id.slider_content, f)
                    //        .commit();

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
