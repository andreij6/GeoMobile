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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.DocumentSentCallback;
import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.QueryMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.SearchMode;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFeatureWindowCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;
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
import com.google.maps.android.clustering.ClusterManager;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
        IViewModeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapLoadedCallback,
        IFeatureWindowCtrl, IMapStatusCtrl
{
    private static final String TAG = GoogleMapFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //region Properties
    public GoogleMap mMap;
    Marker mCurrentLocationMaker;
    Boolean UsingClustering;
    IViewMode mViewMode;
    GoogleApiClient mLocationClient;
    ISlidingPanelManager mPanelManager;
    IMapStateService mMapStateService;
    ILayerManager mLayerManager;
    IMapStatusBarManager mStatusBarManager;
    UiSettings mUiSettings;
    ClusterManager<GeoClusterMarker> mClusterManager;
    QueryRestService mQueryService;
    String mSelectedFeatureId;
    int mSelectedLayerId;
    int mFeatureWindowTabToShow;

    Polygon mHighlightedPolygon;
    Polyline mHighlightedPolyline;
    Marker mHighlightedMarker;

    AnimationDrawable mAnimationDrawable;

    @Bind(R.id.map) MapView mMapView;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
    @Bind(R.id.fab_point) FloatingActionButton mPointQueryBtn;
    @Bind(R.id.fab_close) FloatingActionButton mCloseBtn;
    @Bind(R.id.fab_save) FloatingActionButton mSaveBtn;
    @Bind(R.id.fab_bm_close) FloatingActionButton mBmClose;
    @Bind(R.id.fab_fullscreen_close) FloatingActionButton mFullScreenClose;
    @Bind(R.id.loadingBar) LinearLayout mLoadingBar;
    @Bind(R.id.loadingMessage) TextView mLoadingMessage;
    @Bind(R.id.fourSquareStart) ImageView fourSquareStart;
    @Bind(R.id.fourSquareFinish) ImageView fourSquareFinish;
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
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        } else {
            ((MainActivity) getActivity()).openLayersDrawer();
        }
    }

    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        } else {
            ((MainActivity) getActivity()).openNavigationDrawer();
        }
    }

    @OnClick(R.id.showNavIV2)
    public void showNavigation1(){
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        } else {
            ((MainActivity) getActivity()).openNavigationDrawer();
        }
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

    //@OnClick(R.id.mapOptionsIV)
    //public void showMapOptions(){
    //    if(!mPanelManager.getIsOpen()){
    //        Fragment f = new MapOptionsPanelFragment();
    //        getFragmentManager().beginTransaction()
    //                .replace(R.id.slider_content, f)
    //                .commit();
    //        mPanelManager.halfAnchor();
    //        mPanelManager.touch(false);
    //    } else {
    //        mPanelManager.hide();
    //    }
    //}

    //endregion

    //region Constructors
    public GoogleMapFragment() {
        // Empty constructor required for fragment subclasses
    }
    //endregion

    private static final Boolean LOG_LIFE_CYCLE = true;

    private void LifeCycleLogger(String message){
        if(LOG_LIFE_CYCLE){
            Log.d(TAG, message);
        }
    }

    //region Base Overrides
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        application.setMapFragment(this);
        application.setFeatureWindowCtrl(this);

        LifeCycleLogger("onCreate");

        UsingClustering = false;
        mMapStateService = application.getMapComponent().provideMapStateService();
        mStatusBarManager = application.getStatusBarManager();

    }

    //public void fourSquareAnimation() {
    //    mAnimationDrawable = (AnimationDrawable) fourSquare.getDrawable();
    //
    //    mAnimationDrawable.start();
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        LifeCycleLogger("onCreateView");

        ButterKnife.bind(this, rootView);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){ actionBar.hide(); }

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapScreen());

        application.setMapFragmentPanel(mPanel);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.setup();
        mPanelManager.hide();

        //mPanelManager = new PanelManager.Builder().hide().build();

        mLayerManager = application.getMapComponent().provideLayerManager();

        mQueryService = new QueryRestService();

        initializeGoogleMap(savedInstanceState);

        mNavigationHelper.syncMenu(1);

        return rootView;
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
        LifeCycleLogger("onResume");
        mMapView.onResume();
        setLocationClient();
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
        LifeCycleLogger("onDestroy");
        mLayerManager.clearVisibleLayers();
        if(mMapView != null) {
            mMapView.onDestroy();
        }

        if(mAnimationDrawable != null){
            mAnimationDrawable.stop();
        }
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
        LifeCycleLogger("onStop");
        saveMapState();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LifeCycleLogger("onDestroyView");
        mLayerManager.clearVisibleLayers();
        LifeCycleLogger("Done Clearing Layers");

        if(mViewMode != null){
            mViewMode.Disable(true);
            mViewMode = null;
        }
        LifeCycleLogger("on Destroy View Complete");
    }

    @Override
    public void onPause(){
        super.onPause();
        LifeCycleLogger("onPause");
        if(mMapView != null) {
            mMapView.onPause();
        }

        if(mLocationClient != null && mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new GetLibraryImportFolderTask(requestCode, resultCode, data).execute();
        }
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

                mAnalytics.sendException(e);
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    //endregion

    //region HighLighters
    public void highlight(Polygon feature){
        clearHighlights();

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

    public void highlight(Polyline feature){
        clearHighlights();

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
        clearHighlights();

        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(feature.getPosition());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crosshairs_gps_white_24dp));

        mHighlightedMarker = mMap.addMarker(options);

        zoomToFeature(mHighlightedMarker);
    }

    protected void zoomToFeature(Marker highlightedMarker){
        LatLng position = highlightedMarker.getPosition();

        double lat = 0;

        if(position.latitude > 0){
            lat = position.latitude - .005;
        } else {
            lat = position.latitude + .005;
        }

        LatLng ll = new LatLng(lat, position.longitude);

        float zoom = 15f;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);

        mMap.animateCamera(update);
    }

    protected void zoomToCluster(Marker clusterMarker){
        LatLng position = clusterMarker.getPosition();

        LatLng ll = new LatLng(position.latitude, position.longitude);

        float zoom = mMap.getCameraPosition().zoom + 2;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);

        mMap.animateCamera(update);
    }

    protected void zoomToFeature(Polyline polyline){
        polyZoomToFeature(polyline.getPoints());
    }

    protected void zoomToFeature(Polygon polygon){
        polyZoomToFeature(polygon.getPoints());

    }

    public void polyZoomToFeature(List<LatLng> points){
        LatLngBounds bounds = getBounds(points);

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 20);

        mMap.animateCamera(update);
    }

    private LatLngBounds getBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : points) {
            builder.include(point);
        }

        return builder.build();
    }

    protected int invertColor(int color){
        return (0xFFFFFF - color) | 0xFF000000;
    }

    public void clearHighlights() {
        if(mHighlightedPolyline != null) {
            mHighlightedPolyline.remove();
            mHighlightedPolyline = null;
        }
        if(mHighlightedPolygon != null){
            mHighlightedPolygon.remove();
            mHighlightedPolygon = null;
        }
        if(mHighlightedMarker != null) {
            mHighlightedMarker.remove();
            mHighlightedMarker = null;
        }
    }

    protected Boolean highlightLine(LatLng position){
        Boolean foundMatch = false;

        Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

        double tolerance = calculateTolerance(mMap.getCameraPosition().zoom);

        for (Polyline line : lines) {
            if (PolyUtil.isLocationOnPath(position, line.getPoints(), false, tolerance)) { //idea: reset tolerance by zoom level
                getFeatureWindow(line.getId(), LayerManager.LINE);

                highlight(line);
                foundMatch = true;
                break;
            }
        }

        return foundMatch;
    }

    private double calculateTolerance(float zoom) {
        if(zoom >= 17){
            return 2.0;
        }

        if(zoom < 17 && zoom >= 16){
            return 25.0;
        }

        if(zoom < 16 && zoom >= 15){
            return 75.0;
        }

        if(zoom < 14 && zoom >= 13 ){
            return 150.0;
        }

        if(zoom > 11 && zoom < 12){
            return 450.0;
        }

        if(zoom > 10 && zoom < 11){
            return 750.0;
        }

        if(zoom > 8 && zoom <= 10){
            return 1300.0;
        }

        if(zoom > 5 && zoom <= 8){
            return 1800.0;
        }

        return 3300.0;
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

    public void getFeatureWindow(String id, int shapeCode){
        mStatusBarManager.setMessage(getString(R.string.loading_feature_window));

        mSelectedFeatureId = mLayerManager.getFeatureId(id, shapeCode);
        mSelectedLayerId = mLayerManager.getLayerId(id, shapeCode);

        mQueryService.featureWindow(mSelectedFeatureId, mSelectedLayerId);
    }

    public void refreshFeatureWindow(int tab){
        mStatusBarManager.setMessage(getString(R.string.refreshing_feature_window));

        setFeatureWindowTab(tab);

        mQueryService.featureWindow(mSelectedFeatureId, mSelectedLayerId);

    }

    public void setFeatureWindowTab(int tab){
        mFeatureWindowTabToShow = tab;
    }

    public int getFeatureWindowTab(){
        return mFeatureWindowTabToShow;
    }

    protected void initializeGoogleMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (UsingClustering) {
                    mClusterManager = new ClusterManager<>(getActivity(), mMap);
                    application.setClusterManager(mClusterManager);
                }

                mMap.setMyLocationEnabled(true);

                mUiSettings = mMap.getUiSettings();
                mUiSettings.setMyLocationButtonEnabled(false);

                application.setGoogleMap(mMap);
                mLayerManager.setMap(mMap);

                mMap.setOnMapLoadedCallback(GoogleMapFragment.this);


                try {

                    setLocationClient();

                    MapsInitializer.initialize(getActivity());

                } catch (Exception e) {
                    e.printStackTrace();

                    mAnalytics.sendException(e);
                }

                setMapState();
            }
        });
    }

    private void setLocationClient() {
        mLocationClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(GoogleMapFragment.this)
                .addOnConnectionFailedListener(GoogleMapFragment.this)
                .build();

        mLocationClient.connect();
    }

    public void showFeatureWindow(ParcelableFeatureQueryResponse response) {
        try {
            if (validate(response)) {
                mPanelManager = new PanelManager(GeoPanel.MAP);

                Fragment f = new FeatureWindowPanelFragment().initialize(mHighlightedMarker);

                f.setArguments(response.toBundle());

                application.getMainActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slider_content, f)
                        .commit();

                mPanelManager.halfAnchor();


            } else {
                clearHighlights();

                Toaster("No Data To Display");
            }
        } catch (IndexOutOfBoundsException oe) {
            clearHighlights();

            Toaster("No Data To Display");
        } finally {
            mStatusBarManager.reset();

        }
    }

    private boolean validate(ParcelableFeatureQueryResponse response) throws IndexOutOfBoundsException{
        return response.getFeatureQueryResponse().get(0).getFeatures().size() != 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void hideLoadingBar(){
        mLoadingMessage.setText("");
        mLoadingBar.setVisibility(View.GONE);
    }

    public LinearLayout getLoadingBar() {
        return mLoadingBar;
    }

    public TextView getStatusBarMessage() {
        return mLoadingMessage;
    }

    @Override
    public ImageView getLoadingAnimIV() {
        return fourSquareStart;
    }

    @Override
    public ImageView getFinishLoadingAnimIV() {
        return fourSquareFinish;
    }

    @Override
    public void onMapLoaded() {
        //region Show Feature Window Code
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearHighlights();


                try {
                    if (!UsingClustering) {
                        getFeatureWindow(marker.getId(), LayerManager.POINT);
                    } else {
                        getFeatureWindow(marker.getTitle(), LayerManager.POINT);  //For Clusterer
                    }

                    highlight(marker);

                } catch (Exception e) {
                    mStatusBarManager.reset();

                    if (UsingClustering) {
                        zoomToCluster(marker);

                        mClusterManager.onMarkerClick(marker);
                    }

                    mAnalytics.sendException(e);
                }

                return true;

            }

        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clearHighlights();

                Boolean highlightFound;

                highlightFound = highlightLine(latLng);

                if (!highlightFound) {
                    highlightPolygon(latLng);
                }
            }
        });

        //endregion

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                mLayerManager.showLayers(mMap);

                if(UsingClustering) {
                    mClusterManager.onCameraChange(cameraPosition);
                }
            }
        });

        mLayerManager.showLayers(mMap);
    }

    public void getNextFeature() {
        //Get Next Feature to the right of the current feature
        //1. get LatLng center of current highlight
        LatLng highightedCenter = null;
        int typeCode = 0;

        if(mHighlightedMarker != null){
            typeCode = GeometryTypeCodes.Point;
        }

        if(mHighlightedPolyline != null){
            typeCode = GeometryTypeCodes.Line;
        }

        if(mHighlightedPolygon != null){
            typeCode = GeometryTypeCodes.Polygon;
        }

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, true);
        }
    }


    public void simulateClick(final LatLng latLng) {

        if(latLng == null){
            Toaster("No Next Feature");
            return;
        }

        float zoom = 15f;

        double lat = 0;

        if(latLng.latitude > 0){
            lat = latLng.latitude - .005;
        } else {
            lat = latLng.latitude + .005;
        }

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, latLng.longitude), zoom);

        mMap.animateCamera(update);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Point point = mMap.getProjection().toScreenLocation(latLng);

                long touchTime = SystemClock.uptimeMillis();

                MotionEvent simulationEvent = MotionEvent.obtain(touchTime, touchTime,
                        MotionEvent.ACTION_DOWN, point.x, point.y, 0);
                mMapView.dispatchTouchEvent(simulationEvent);

                simulationEvent = MotionEvent.obtain(touchTime, touchTime,
                        MotionEvent.ACTION_UP, point.x, point.y, 0);

                mMapView.dispatchTouchEvent(simulationEvent);
            }
        }, 1100);

    }

    public void getPrevious() {
        int typeCode = 0;

        if(mHighlightedMarker != null){
            typeCode = GeometryTypeCodes.Point;
        }

        if(mHighlightedPolyline != null){
            typeCode = GeometryTypeCodes.Line;
        }

        if(mHighlightedPolygon != null){
            typeCode = GeometryTypeCodes.Polygon;
        }

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, false);
        }
    }

    public Polygon getHighlightedPolygon() {
        return mHighlightedPolygon;
    }

    public void centerMap(LatLng center) {
        CameraUpdate update = CameraUpdateFactory.newLatLng(center);

        mMap.animateCamera(update);
    }

    public void rezoomToHighlight() {

        if(mHighlightedMarker != null){
            zoomToFeature(mHighlightedMarker);
        }

        if(mHighlightedPolygon != null){
            zoomToFeature(mHighlightedPolygon);
        }

        if(mHighlightedPolyline != null){
            zoomToFeature(mHighlightedPolyline);
        }
    }


    protected class GetLibraryImportFolderTask extends AsyncTask<Void, Void, Folder> {

        int RequestCode;
        int ResultCode;
        Intent Data;
        TreeService mTreeService;
        DataHelper mHelper;
        IDocumentTreeService mUploader;

        public GetLibraryImportFolderTask(int requestCode, int resultCode, Intent data){
            RequestCode = requestCode;
            ResultCode = resultCode;
            Data = data;
            mTreeService = application.getRestAdapter().create(TreeService.class);
            mHelper = new DataHelper();
        }

        @Override
        protected Folder doInBackground(Void... params) {
            List<Folder> documentsTree = mTreeService.getDocuments();

            Folder rootFolder = documentsTree.get(0);

            List<Folder> allFolders = mHelper.getFoldersRecursively(rootFolder, rootFolder.getParent());

            Folder result = null;

            for(Folder folder : allFolders){
                if(folder.getIsImportFolder()){
                    result = folder;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Folder importFolder) {
            mUploader = application.getTreeServiceComponent().provideDocumentTreeService();

            int layerId = application.getFeatureWindowLayerId();
            String featureId = application.getFeatureWindowFeatureId();

            ISendFileCallback sendFileCallback = new DocumentSentCallback(layerId, featureId);

            if (RequestCode == MainActivity.MediaConstants.PICK_FILE_REQUEST_FEATUREWINDOW) {

                mUploader.sendDocument(importFolder, Data.getData(), sendFileCallback);

            }

            if(RequestCode == MainActivity.MediaConstants.PICK_IMAGE_REQUEST_FEATUREWINDOW){

                mUploader.sendPickedImage(importFolder, Data.getData(), sendFileCallback);

            }

            if(RequestCode == MainActivity.MediaConstants.TAKE_IMAGE_REQUEST_FEATUREWINDOW) {

                mUploader.sendTakenImage(importFolder, application.mMediaUri, sendFileCallback);

            }
        }
    }

}
