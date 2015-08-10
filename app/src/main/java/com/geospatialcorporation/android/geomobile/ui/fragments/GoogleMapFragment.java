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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.LayerManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.QueryMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.SearchMode;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapTypeSelectDialogFragment;
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
    Menu mMenu;
    private Layer mZoomToLayer;
    //endregion

    //region OnClicks
    //@SuppressWarnings("unused")
    //@OnClick(R.id.fab)
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

        MarkerOptions options = new MarkerOptions();
        options.position(ll);

        //mCurrentLocationMaker = mMap.addMarker(options);

    }

    //@SuppressWarnings("unused")
    //@OnClick(R.id.fab_layers)
    public void showLayersDrawer(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().OpenLayerDrawer());
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
        application.setMapFragment(this);
        mMapStateService = application.getMapComponent().provideMapStateService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.inject(this, rootView);
        SetTitle(R.string.geounderground);

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapScreen());

        application.setMapFragmentPanel(mPanel);
        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.setup();
        mPanelManager.touch(false);

        initializeGoogleMap(savedInstanceState);

        mLayerManager = application.getLayerManager();
        mLayerManager.showLayers();

        //region Show Feature Window Code

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearHighlights();

                if(mCurrentLocationMaker != null && marker.getId().equals(mCurrentLocationMaker.getId())) {
                    Toaster("Current Location");
                    return  true;
                } else {
                    getFeatureWindow(marker.getId(), LayerManager.POINT);

                    highlight(marker);

                    return false;
                }

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clearHighlights();

                Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

                for (Polyline line : lines) {
                    if (PolyUtil.isLocationOnPath(latLng, line.getPoints(), false, 200.0)) { //idea: reset tolerance by zoom level
                        getFeatureWindow(line.getId(), LayerManager.LINE);

                        highlight(line);

                    }
                }

                Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

                for (Polygon ss : polygons) {
                    if (PolyUtil.containsLocation(latLng, ss.getPoints(), true)) {
                        getFeatureWindow(ss.getId(), LayerManager.POLYGON);

                        highlight(ss);
                    }

                }


            }
        });

        //endregion

        return rootView;
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
    }

    protected void highlight(Polyline feature){
        PolylineOptions options = new PolylineOptions();
        options.color(invertColor(feature.getColor()));

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        mHighlightedPolyline = mMap.addPolyline(options);
    }

    protected void highlight(Marker feature){
        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(feature.getPosition());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crosshairs_gps_white_24dp));

        mHighlightedMarker = mMap.addMarker(options);
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
    //endregion

    protected void getFeatureWindow(String id, int shapeCode){
        String featureId = mLayerManager.getFeatureId(id, shapeCode);
        int layerId = mLayerManager.getLayerId(id, shapeCode);
        QueryRestService QueryService = new QueryRestService();
        QueryService.featureWindow(featureId, layerId);
    }

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
        mLayerManager.showLayers();
        mLocationClient.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                mAnalytics.trackClick(new GoogleAnalyticEvent().ChangeBaseMap());
                MapTypeSelectDialogFragment m = new MapTypeSelectDialogFragment();
                m.setContext(getActivity());
                m.setMap(mMap);
                m.show(getFragmentManager(), "styles");
                return true;
            //case R.id.action_fullscreen:
            //    mPanelManager.hide();
            //    setViewMode(fullScreenSetup());
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
    //                            activity.getRightDrawer(),
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
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    public void zoomToLayer() {
        if(mZoomToLayer != null) {
            if (mZoomToLayer.getExtent() != null) {
                LatLng first = mZoomToLayer.getExtent().getMaxLatLng();
                LatLng second = mZoomToLayer.getExtent().getMinLatLng();

                LatLngBounds bounds = new LatLngBounds(second, first);

                int padding = 50;
                CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(u);
            }

            mZoomToLayer = null;
        }
    }

    public void setZoomToLayer(Layer zoomToLayer) {
        mZoomToLayer = zoomToLayer;
    }
}
