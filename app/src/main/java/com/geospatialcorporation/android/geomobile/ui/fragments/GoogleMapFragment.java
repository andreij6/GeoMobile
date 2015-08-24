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
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.QueryMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.SearchMode;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureWindowPanelFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.google.maps.android.clustering.*;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
        IViewModeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    private static final String TAG = GoogleMapFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //region Properties
    public GoogleMap mMap;
    Marker mCurrentLocationMaker;
    IViewMode mViewMode;
    GoogleApiClient mLocationClient;
    ISlidingPanelManager mPanelManager;
    IMapStateService mMapStateService;
    ILayerManager mLayerManager;
    UiSettings mUiSettings;
    ClusterManager<GeoClusterMarker> mClusterManager;

    Polygon mHighlightedPolygon;
    Polyline mHighlightedPolyline;
    Marker mHighlightedMarker;

    @InjectView(R.id.map) MapView mMapView;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @InjectView(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
    @InjectView(R.id.fab_point) FloatingActionButton mPointQueryBtn;
    @InjectView(R.id.fab_close) FloatingActionButton mCloseBtn;
    @InjectView(R.id.fab_save) FloatingActionButton mSaveBtn;
    @InjectView(R.id.fab_bm_close) FloatingActionButton mBmClose;
    @InjectView(R.id.fab_fullscreen_close) FloatingActionButton mFullScreenClose;
    @InjectView(R.id.loadingBar) LinearLayout mLoadingBar;
    @InjectView(R.id.loadingMessage) TextView mLoadingMessage;
    Menu mMenu;
    //endregion

    //region OnClicks
    @SuppressWarnings("unused")
    @OnClick(R.id.getLocationIB)
    public void getLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        mAnalytics.trackClick(new GoogleAnalyticEvent().CurrentLocation());

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
            handleNewLocation(currentLocation);
        }
    }

    protected void handleNewLocation(Location currentLocation) {

        if(mCurrentLocationMaker != null){
            mCurrentLocationMaker.remove();
        }

        LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 16);

        mMap.animateCamera(update);

    }

    @OnClick(R.id.showLayersIV)
    public void showLayersDrawer(){
        ((MainActivity)getActivity()).openLayersDrawer();
    }

    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.showNavIV2)
    public void showNavigation1(){

        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.fab_fullscreen_close)
    public void closeFullScreenMode(){
        //probably an expensive way to reset after fullscreen mode but I havent found a good solution yet to disable fullscreen mode
        onStop();
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    @OnClick(R.id.extentIB)
    public void zoomToExtent(){
        Extent extent = mLayerManager.getFullExtent();
        mLayerManager.zoomToExtent(extent);
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
        application.setMapFragment(this);
        mMapStateService = application.getMapComponent().provideMapStateService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.inject(this, rootView);
        ((MainActivity)getActivity()).getSupportActionBar().hide();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapScreen());

        application.setMapFragmentPanel(mPanel);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.setup();
        mPanelManager.hide();

        mLayerManager = application.getMapComponent().provideLayerManager();

        initializeGoogleMap(savedInstanceState);

        //mLayerManager.showLayers();

        mNavigationHelper.syncMenu(1);

        return rootView;
    }

    protected void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(getActivity(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                mLayerManager.showLayers(mMap);

                mClusterManager.onCameraChange(cameraPosition);
            }
        });
        mMap.setOnMarkerClickListener(mClusterManager);


    }

    //region HighLighters
    protected void highlight(Polygon feature){
        PolygonOptions options = new PolygonOptions();
        options.strokeColor(invertColor(feature.getStrokeColor()));
        options.fillColor(invertColor(feature.getFillColor()));
        options.strokeWidth(feature.getStrokeWidth());

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        mHighlightedPolygon = mMap.addPolygon(options);

        zoomToFeature(mHighlightedPolygon);
    }

    protected void highlight(Polyline feature){
        PolylineOptions options = new PolylineOptions();
        options.color(invertColor(feature.getColor()));

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        mHighlightedPolyline = mMap.addPolyline(options);

        zoomToFeature(mHighlightedPolyline);
    }

    protected void highlight(Marker feature){
        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(feature.getPosition());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crosshairs_gps_white_24dp));

        mHighlightedMarker = mMap.addMarker(options);

        zoomToFeature(mHighlightedMarker);
    }

    protected void zoomToFeature(Marker highlightedMarker){
        LatLng position = highlightedMarker.getPosition();

        LatLng ll = new LatLng(position.latitude, position.longitude);

        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);

        mMap.animateCamera(update);
    }

    protected void zoomToFeature(Polyline polyline){
        polyZoomToFeature(polyline.getPoints());
    }

    protected void zoomToFeature(Polygon polygon){
        polyZoomToFeature(polygon.getPoints());

    }

    public void polyZoomToFeature(List<LatLng> points){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : points) {
            builder.include(point);
        }

        LatLngBounds bounds = builder.build();

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 20);

        mMap.animateCamera(update);
    }

    protected int invertColor(int color){
        return (0xFFFFFF - color) | 0xFF000000;
    }

    public void clearHighlights() {
        if(mHighlightedPolyline != null) {
            mHighlightedPolyline.remove();
        }
        if(mHighlightedPolygon != null){
            mHighlightedPolygon.remove();
        }
        if(mHighlightedMarker != null) {
            mHighlightedMarker.remove();
        }
    }

    protected Boolean highlightLine(LatLng position){
        Boolean foundMatch = false;

        Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

        for (Polyline line : lines) {
            if (PolyUtil.isLocationOnPath(position, line.getPoints(), false, 200.0)) { //idea: reset tolerance by zoom level
                getFeatureWindow(line.getId(), LayerManager.LINE);

                highlight(line);
                foundMatch = true;
                break;
            }
        }

        return foundMatch;
    }

    protected void  highlightPolygon(LatLng position){
        Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

        for (Polygon ss : polygons) {
            if (PolyUtil.containsLocation(position, ss.getPoints(), true)) {
                getFeatureWindow(ss.getId(), LayerManager.POLYGON);

                highlight(ss);
                break;
            }

        }
    }
    //endregion

    protected void getFeatureWindow(String id, int shapeCode){
        showLoadingMessage("Loading Feature Window");

        String featureId = mLayerManager.getFeatureId(id, shapeCode);
        int layerId = mLayerManager.getLayerId(id, shapeCode);
        QueryRestService QueryService = new QueryRestService();
        QueryService.featureWindow(featureId, layerId);
    }

    protected void initializeGoogleMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setMyLocationEnabled(true);

                mUiSettings = mMap.getUiSettings();
                mUiSettings.setMyLocationButtonEnabled(false);

                application.setGoogleMap(mMap);
                mLayerManager.setMap(mMap);

                mLocationClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(GoogleMapFragment.this)
                        .addOnConnectionFailedListener(GoogleMapFragment.this)
                        .build();
                try {

                    MapsInitializer.initialize(GoogleMapFragment.this.getActivity());

                    mLocationClient.connect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                setUpClusterer();
                application.setClusterManager(mClusterManager);

                //region Show Feature Window Code

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        clearHighlights();

                        try {
                            //getFeatureWindow(marker.getId(), LayerManager.POINT);
                            getFeatureWindow(marker.getTitle(), LayerManager.POINT);  //For Clusterer

                            highlight(marker);

                        } catch (Exception e) {
                            mClusterManager.onMarkerClick(marker);
                            Log.e(TAG, e.getMessage());
                        }

                        return true;

                    }

                });

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        clearHighlights();

                        Boolean highlightFound = false;

                        highlightFound = highlightLine(latLng);

                        if(highlightFound == false) {
                            highlightPolygon(latLng);
                        }
                    }
                });

                //endregion

                setMapState();

                mLayerManager.showLayers(mMap);
            }
        });



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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_show_layers:
                showLayersDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLayerManager.clearVisibleLayers();
        mMapView.onDestroy();
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
        mLayerManager.clearVisibleLayers();
        if(mViewMode != null){
            mViewMode.Disable(true);
            mViewMode = null;
        }
        //mPanelManager.hide();
       // mPanelManager.collapse();
    }

    @Override
    public void onPause(){
        super.onPause();

        mLayerManager.clearVisibleLayers();
        mMapView.onPause();
        mLocationClient.disconnect();
    }

    //endregion

    //region ViewModeSetups
    protected IViewMode querySetup() {
        mAnalytics.trackClick(new GoogleAnalyticEvent().QueryModeInit());

        return new QueryMode.Builder()
                .setDependents(mMap, mListener, getActivity())
                .setControls(mBoxQueryBtn, mPointQueryBtn, mCloseBtn, getActivity().getSupportFragmentManager())
                .setupUI()
                .create();

    }

    protected IViewMode searchSetup() {
        mAnalytics.trackClick(new GoogleAnalyticEvent().QuickSearchInit());

        return new SearchMode.Builder()
                        .init(getActivity().getSupportFragmentManager(), mPanelManager)
                        .create();
    }

    //protected IViewMode fullScreenSetup(){
    //    mAnalytics.sendClickEvent(R.string.full_screen_mode);
    //    MainActivity activity = (MainActivity)getActivity();
    //    return new FullScreenMode.Builder()
    //                    .create(activity.getSupportActionBar(),
    //                            activity.getDrawerLayout(),
    //                            mPanel, mFullScreenClose);
    //}
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

    //public void setViewMode(String mode){
    //    switch(mode){
    //        case ViewModes.BOOKMARK:
    //            GeoDialogHelper.showBookmarks(getActivity(), getFragmentManager(), mSaveBtn, mBmClose, mPanel, mMap);
    //            mPanelManager.collapse();
    //            break;
    //        case ViewModes.QUERY:
    //            setViewMode(querySetup());
    //            break;
    //        case ViewModes.QUICKSEARCH:
    //            setViewMode(searchSetup());
    //            break;
    //        default:
    //            Toaster("Mode Not Set");
    //            break;
    //    }
    //}



    public void resetViewMode() {
        mViewMode = null;
    }
    //endregion

    protected void saveMapState() {
        mMapStateService.saveMapState(new MapStateSaveRequest(mMap));
    }

    protected void setMapState() {
        CameraPosition position = mMapStateService.getSavedCameraPosition();
        Integer mapType = mMapStateService.getSavedMapType();

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
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    //endregion

    public void showFeatureWindow(ParcelableFeatureQueryResponse response) {
        mPanelManager = new PanelManager(GeoPanel.MAP);

        Fragment f = new FeatureWindowPanelFragment();

        f.setArguments(response.toBundle());

        application.getMainActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.slider_content, f)
                .commit();

        mPanelManager.halfAnchor();

        hideLoadingBar();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void showLoadingMessage(String message){
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingMessage.setText(message);
    }

    public void hideLoadingBar(){
        mLoadingMessage.setText("");
        mLoadingBar.setVisibility(View.GONE);
    }

}
