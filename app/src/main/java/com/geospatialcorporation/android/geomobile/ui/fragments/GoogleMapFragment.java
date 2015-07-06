/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.constants.ViewModes;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.FullScreenMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.QueryMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.SearchMode;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapTypeSelectDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
        IViewModeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    //region Properties
    public GoogleMap mMap;
    IViewMode mViewMode;
    GoogleApiClient mLocationClient;
    ISlidingPanelManager mPanelManager;
    @InjectView(R.id.map) MapView mMapView;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @InjectView(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
    @InjectView(R.id.fab_point) FloatingActionButton mPointQueryBtn;
    @InjectView(R.id.fab_close) FloatingActionButton mCloseBtn;
    @InjectView(R.id.fab_save) FloatingActionButton mSaveBtn;
    @InjectView(R.id.fab_bm_close) FloatingActionButton mBmClose;
    @InjectView(R.id.fab_fullscreen_close) FloatingActionButton mFullScreenClose;
    Menu mMenu;
    //endregion

    //region OnClicks
    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void getLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        mAnalytics.sendClickEvent(R.string.current_location);

        if(currentLocation == null){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(getString(R.string.location_not_found))
                    .setPositiveButton(getString(R.string.enable_gps),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent gpsOptions = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                            startActivity(gpsOptions);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 13);

            mMap.animateCamera(update);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_layers)
    public void showLayersDrawer(){
        mAnalytics.sendClickEvent(R.string.show_layers);
        DrawerLayout mDrawerLayout = ((MainActivity)getActivity()).getRightDrawer();
        View layerView = ((MainActivity)getActivity()).getLayerListView();

        mDrawerLayout.openDrawer(layerView);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_fullscreen_close)
    public void closeFullScreenMode(){
        //probably an expensive way to reset after fullscreen mode but I havent found a good solution yet to disable fullscreen mode
        onStop();
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    //endregion

    //region Constructors
    public GoogleMapFragment() {
        // Empty constructor required for fragment subclasses
    }
    //endregion

    //region Base Overrides
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        if(mMapView != null){
            mMapView.onCreate(savedInstance);
        }

        application.setMapFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.inject(this, rootView);
        SetTitle(R.string.app_name);

        mAnalytics.sendScreenName(R.string.map_screen);

        application.setMapFragmentPanel(mPanel);
        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.setup();
        mPanelManager.touch(false);

        initializeGoogleMap(savedInstanceState);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                application.getLayerManager().showLayers();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //mPanelManager.anchor();
                String featureId = application.getLayerManager().getFeatureId(marker.getId());
                int layerId = application.getLayerManager().getLayerId(marker.getId());

                QueryRestService QueryService = new QueryRestService();
                QueryService.featureWindow(featureId, layerId);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toaster(latLng.latitude + ":" + latLng.longitude);
            }
        });

        return rootView;
    }

    //region Args Handler -
    //clicking the show layer button from another fragment should zoom to the extent on the map
    //sometimes this runs thru before mMap is ready I tried placing the call after initializeGoogleMap but didnt work
    protected void handleArgs() {
        Bundle args = getArguments();

        if(args != null && args.getParcelable(Layer.LAYER_INTENT) != null){
            Layer layer = args.getParcelable(Layer.LAYER_INTENT);
            zoomToLayer(layer);
        }
    }

    protected void zoomToLayer(Layer layer){
        if(layer.getExtent() != null) {
            LatLng first = layer.getExtent().getMaxLatLng();
            LatLng second = layer.getExtent().getMinLatLng();

            LatLngBounds bounds = new LatLngBounds(second, first);

            int padding = 50;
            CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.animateCamera(u);
        }
    }
    //endregion

    protected void initializeGoogleMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);

        mMap = mMapView.getMap();

        application.setGoogleMap(mMap);

        mLocationClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        try {
            MapsInitializer.initialize(this.getActivity());

            mLocationClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setMapState();
        mPanelManager.collapse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                mAnalytics.sendClickEvent(R.string.change_basemap);
                MapTypeSelectDialogFragment m = new MapTypeSelectDialogFragment();
                m.setContext(getActivity());
                m.setMap(mMap);
                m.show(getFragmentManager(), "styles");
                return true;
            case R.id.action_fullscreen:
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                setViewMode(fullScreenSetup());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        saveMapState();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(mViewMode != null){
            mViewMode.Disable(true);
            mViewMode = null;
        }

        mPanelManager.collapse();
    }

    @Override
    public void onPause(){
        mMapView.onPause();
        super.onPause();
    }

    //endregion

    //region ViewModeSetups
    private IViewMode querySetup() {
        mAnalytics.sendClickEvent(R.string.query_mode);

        return new QueryMode.Builder()
                .setDependents(mMap, mListener, getActivity())
                .setControls(mBoxQueryBtn, mPointQueryBtn, mCloseBtn, getActivity().getSupportFragmentManager())
                .setupUI()
                .create();

    }

    protected IViewMode searchSetup() {
        mAnalytics.sendClickEvent(R.string.quicksearch_mode);

        return new SearchMode.Builder()
                        .init(getActivity().getSupportFragmentManager(), mPanelManager)
                        .create();
    }

    protected IViewMode fullScreenSetup(){
        mAnalytics.sendClickEvent(R.string.full_screen_mode);
        MainActivity activity = (MainActivity)getActivity();
        return new FullScreenMode.Builder()
                        .create(activity.getSupportActionBar(),
                                activity.getRightDrawer(),
                                mPanel, mFullScreenClose);
    }
    //endregion

    //region ViewMode Listener
    @Override
    public void setViewMode(IViewMode mode) {

        if(mViewMode != null){
            if(mViewMode.isSame(mode)){
                mViewMode.Disable(true);
                mViewMode = null;
            } else {
                mViewMode.Disable(false);
                mViewMode = null;
                mViewMode = mode;
            }

        } else {
            mViewMode = mode;
        }
    }

    public void setViewMode(String mode){
        switch(mode){
            case ViewModes.BOOKMARK:
                GeoDialogHelper.showBookmarks(getActivity(), getFragmentManager(), mSaveBtn, mBmClose, mPanel, mMap);
                mPanelManager.collapse();
                break;
            case ViewModes.QUERY:
                setViewMode(querySetup());
                break;
            case ViewModes.QUICKSEARCH:
                setViewMode(searchSetup());
                break;
            default:
                Toaster("Mode Not Set");
                break;
        }
    }



    public void resetViewMode() {
        mViewMode = null;
    }
    //endregion

    private void saveMapState() {
        MapStateManager msm = new MapStateManager(getActivity());
        msm.saveMapState(mMap);
    }

    private void setMapState() {
        MapStateManager msm = new MapStateManager(getActivity());

        CameraPosition position = msm.getSavedCameraPosition();
        Integer mapType = msm.getSavedMapType();

        mMap.setMapType(mapType);

        if(position != null){
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
        }
    }

    //region GoogleAPI Client Interface
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    //endregion


}
