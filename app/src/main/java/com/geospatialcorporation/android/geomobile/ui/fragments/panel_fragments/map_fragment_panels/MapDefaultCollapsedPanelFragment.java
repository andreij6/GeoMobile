package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerEditDialog;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.map_settings)
    public void mapSettings(){
        Toaster("Map Settings Not Implemented");

        //1. expand the panel to show base map options

        mPanelManager.collapse();
    }

    @OnClick(R.id.edit_layer)
    public void editLayer(){
        //1. show dialog of layers to choose to edit
        ILayerEditDialog dialog = application.getUIHelperComponent().provideLayerEditDialog();

        dialog.editLayers(getActivity(), getFragmentManager());

        mPanelManager.collapse();
    }

    @OnClick(R.id.quick_search)
    public void quickSearch(){
        Toaster("Quick Search Not Implemented");

        //1. expand the panel and to show search
        Fragment f = new QuickSearchPanelFragment();

        application.getMainActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.slider_content, f)
                .commit();


        mPanelManager.expand();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_map_collapsed, container, false);
        ButterKnife.bind(this, mView);

        mPanelManager = new PanelManager(GeoPanel.MAP);

        mPanelManager.touch(false);

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
