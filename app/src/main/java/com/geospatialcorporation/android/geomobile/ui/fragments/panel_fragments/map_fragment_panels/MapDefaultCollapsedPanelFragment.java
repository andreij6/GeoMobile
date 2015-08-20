package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.constants.ViewModes;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/5/2015.
 */
public class MapDefaultCollapsedPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = MapDefaultCollapsedPanelFragment.class.getSimpleName();

    View mView;
    ISlidingPanelManager mPanelManager;
    ILayerManager mLayerManager;

    //@InjectView(R.id.title) TextView Title;
    //@InjectView(R.id.bookmarkBtn) Button mBookmark;
    //@InjectView(R.id.quickSearchBtn) Button mSearch;
    //@InjectView(R.id.queryBtn) Button mQuery;
    //@InjectView(R.id.panelAnchor) ImageView mAnchor;

    @OnClick(R.id.currentLocation)
    public void currentLocation(){
        GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

        mapFragment.getLocation();
    }

    @OnClick(R.id.showLayers)
    public void showLayers(){
        GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

        mapFragment.showLayersDrawer();
    }

    @OnClick(R.id.extent)
    public void zoomToExtent(){
        if(mLayerManager == null) {
            mLayerManager = application.getMapComponent().provideLayerManager();
        }

        Extent extent = mLayerManager.getFullExtent();
        mLayerManager.zoomToExtent(extent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_map_collapsed, container, false);
        ButterKnife.inject(this, mView);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.touch(false);

        //mBookmark.setOnClickListener(setBookmarkMode);
        //mSearch.setOnClickListener(performQuickSearch);
        //mQuery.setOnClickListener(setQueryMode);
        //mAnchor.setOnClickListener(anchorPanel);
        //Title.setOnClickListener(anchorPanel);

        return mView;
    }

    //region ClickListeners
    //protected View.OnClickListener setBookmarkMode = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        setViewMode(ViewModes.BOOKMARK);
    //    }
    //};

    //protected View.OnClickListener performQuickSearch = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        setViewMode(ViewModes.QUICKSEARCH);
    //    }
    //};

    //protected View.OnClickListener setQueryMode = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        setViewMode(ViewModes.QUERY);
    //    }
    //};

    //endregion

    //private void setViewMode(String viewMode) {
    //    GoogleMapFragment mapFragment = (GoogleMapFragment)(((MainActivity) getActivity()).getContentFragment());
    //    mapFragment.setViewMode(viewMode);
    //}

}
