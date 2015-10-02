package com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IGeoUndergroundMap;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFeatureWindowCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureWindowPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.TabletFeatureWindowPanelFragment;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TabletMapFragment extends Fragment
    implements IFeatureWindowCtrl
{
    public GoogleMap mMap;
    IGeoUndergroundMap mGeoMap;
    ILayerManager mLayerManager;

    @Bind(R.id.map) MapView mMapView;
    @Bind(R.id.getLocationIB) ImageButton mGPSbtn;

    @OnClick(R.id.extentIB)
    public void zoomToExtent(){
        Extent extent = mLayerManager.getFullExtent();
        mLayerManager.zoomToExtent(extent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.setFeatureWindowCtrl(this);

        mGeoMap = application.getMapComponent().provideGeoUndergroundMap();
        mGeoMap.setup(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tablet_fragment_map, container, false);

        ButterKnife.bind(this, rootView);

        mLayerManager = application.getLayerManager();

        mGeoMap.initializeMap(savedInstanceState, mMapView, this);

        mGeoMap.getLocationBtn(mGPSbtn);

        return rootView;
    }

    //region Neccessary Map LifeCycle Methods
    @Override
    public void onResume() {
        super.onResume();
        mGeoMap.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGeoMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mGeoMap.onLowMemory();
    }

    @Override
    public void onPause(){
        super.onPause();
        mGeoMap.onPause();

    }
    //endregion

    public void setMap(GoogleMap map) {
        mMap = map;
    }

    @Override
    public void showFeatureWindow(ParcelableFeatureQueryResponse response) {
        if(mGeoMap.validate(response)){
            Fragment f = new TabletFeatureWindowPanelFragment();

            f.setArguments(response.toBundle());

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.info_frame, f)
                    .commit();

            ((MainTabletActivity)getActivity()).featureWindow();
        } else {
            mGeoMap.clearHighlights();

            Toast.makeText(getActivity(), "No Data To Display", Toast.LENGTH_LONG).show();

            //mStatusBarManager.reset();
        }
    }
}
