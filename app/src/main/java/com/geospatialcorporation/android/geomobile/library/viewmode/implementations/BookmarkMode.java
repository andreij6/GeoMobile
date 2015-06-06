package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.view.View;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
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
    public void Disable() {
        mBuilder.Reset();
    }

    public static class Builder {

        FloatingActionButton mSave;
        FloatingActionButton mClose;
        SlidingUpPanelLayout mPanel;
        GoogleMap mMap;

       public Builder init(FloatingActionButton save, FloatingActionButton close, SlidingUpPanelLayout panel, final GoogleMap map){
            mSave = save;
           mClose = close;
           mPanel = panel;
           mMap = map;

           save.setVisibility(View.VISIBLE);
           close.setVisibility(View.VISIBLE);
           mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

           SetClickEvents(save, close);

           return this;
       }

        protected void SetClickEvents(FloatingActionButton save, FloatingActionButton close) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapStateManager msm = new MapStateManager(application.getAppContext());
                    //msm.saveMapStateForBookMark(mMap);
                    Toast.makeText(application.getAppContext(), "Save: Not Implemented", Toast.LENGTH_LONG).show();
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reset();
                }
            });
        }

        protected void Reset(){
            mSave.setVisibility(View.GONE);
            mClose.setVisibility(View.GONE);
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        public BookmarkMode create(){
            return new BookmarkMode(this);
        }
    }
}
