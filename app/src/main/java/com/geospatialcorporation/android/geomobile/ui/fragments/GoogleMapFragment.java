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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.DocumentSentCallback;
import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.map.EditLayerMapQueryRequestCallback;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.ILayerEditor;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.LineLayerEditor;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.PointLayerEditor;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.PolygonLayerEditor;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.Options;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFeatureWindowCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.RestoreSettings;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.EditLinePanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.EditPointPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.EditPolygonPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureWindowPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.MapDefaultCollapsedPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
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
    MapFragmentRestoreSettings mRestoreSettings;
    SettingsConfig mRestoreOptions;

    ILayerEditor mEditor;

    List<Polygon> mHighlightedPolygons;
    List<Polyline> mHighlightedPolylines;

    Polygon mHighlightedPolygon;
    Polyline mHighlightedPolyline;
    Marker mHighlightedMarker;

    final static String RESTORE_SETTINGS_KEY = MapFragmentRestoreSettings.class.getSimpleName();

    AnimationDrawable mAnimationDrawable;

    @Bind(R.id.map) MapView mMapView;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.loadingBar) LinearLayout mLoadingBar;
    @Bind(R.id.loadingMessage) TextView mLoadingMessage;
    @Bind(R.id.fourSquareStart) ImageView fourSquareStart;
    @Bind(R.id.fourSquareFinish) ImageView fourSquareFinish;
    @Bind(R.id.optionsIV) ImageView mMapOptionsIV;
    @Bind(R.id.map_actionbar) RelativeLayout mMapActionBar;
    @Bind(R.id.undoBtn) FloatingActionButton mUndoBtn;
    @Bind(R.id.redoBtn) FloatingActionButton mRedoBtn;
    @Bind(R.id.save) ImageView mEditLayerSave;
    @Bind(R.id.cancel) ImageView mEditLayerCancel;
    @Bind(R.id.editLayerTitle) TextView mEditLayerTitle;
    @Bind(R.id.edit_actionBar) RelativeLayout mEditActionBar;
    @Bind(R.id.getLocationIB) ImageButton mLocationBtn;
    @Bind(R.id.extentIB) ImageButton mExtentBtn;
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

    @Nullable
    @OnClick(R.id.libraryIV)
    public void goToLibrary(){
        Fragment f = new LibraryFragment();

        f.setArguments(LibraryFragment.rootBundle());

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, f)
                .commit();
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
        }

        ((MainActivity) getActivity()).openLayersDrawer();
    }

    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        }

        ((MainActivity) getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.showNavIV2)
    public void showNavigation1(){
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        }

        ((MainActivity) getActivity()).openNavigationDrawer();

    }

    @OnClick(R.id.optionsIV)
    public void optionsIV(){
        if(mPanelManager.getIsOpen()){
            mPanelManager.collapse();
        } else {
            Fragment f = new MapDefaultCollapsedPanelFragment();

            application.getMainActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor(-0.40f);
        }
    }

    @OnClick(R.id.save)
    public void saveEdits(){
        mEditor.saveEdits();

        setStandardMapOnClicks();

        mEditor.resetMapFragment();

        resetMapFragment();
    }

    @OnClick(R.id.cancel)
    public void cancelEdits(){
        setStandardMapOnClicks();

        mEditor.resetMapFragment();

        resetMapFragment();
    }

    private void resetMapFragment() {
        clearHighlights();

        switchActionBarVisibility(mMapActionBar, mEditActionBar);

        mLocationBtn.setVisibility(View.VISIBLE);
        mExtentBtn.setVisibility(View.VISIBLE);

        mRedoBtn.setVisibility(View.GONE);
        mUndoBtn.setVisibility(View.GONE);

        mLayerManager.clearVisibleLayers();

        mPanelManager.collapse();

        application.setEditingLayerMode(false);

        zoomToExtent();
    }

    protected void setStandardMapOnClicks() {
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

                    highlight(marker, true);

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
                    highlightFound = highlightPolygon(latLng);
                }

                if (!highlightFound) {
                    closeFeatureWindow();
                }
            }
        });

        //endregion

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                mLayerManager.showLayers(mMap);

                if (UsingClustering) {
                    mClusterManager.onCameraChange(cameraPosition);
                }
            }
        });
    }

    private void closeFeatureWindow() {
        if (application.getIsLandscape()) {
            MainActivity activity = (MainActivity) getActivity();
            activity.closeDetailFragment();
        } else {
            mPanelManager.hide();
        }
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

    private static final Boolean LOG_LIFE_CYCLE = true;

    //region Base Overrides
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        application.setMapFragment(this);
        application.setFeatureWindowCtrl(this);

        mEditor = null;

        UsingClustering = false;
        mMapStateService = application.getMapComponent().provideMapStateService();
        mStatusBarManager = application.getStatusBarManager();

        mHighlightedPolygons = new ArrayList<>();
        mHighlightedPolylines = new ArrayList<>();
        mRestoreOptions = new SettingsConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){ actionBar.hide(); }

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapScreen());

        application.setMapFragmentPanel(mPanel);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.setup();
        mPanelManager.hide();

        mLayerManager = application.getMapComponent().provideLayerManager();

        mQueryService = new QueryRestService();

        initializeGoogleMap();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mMapView.onCreate(mapViewSavedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        //hacky
        //problem: in landscape the map doesnt show until a drawer is opened when navigating back to the map
        //hacky solution: open and close the drawer


    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setLocationClient();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mMapView != null) {
            final Bundle mapViewSaveState = new Bundle(outState);
            mMapView.onSaveInstanceState(mapViewSaveState);
            outState.putBundle("mapViewSaveState", mapViewSaveState);
        }

        if(featureWindowIsShowing()){
            setRestoreSettings();
        }

        super.onSaveInstanceState(outState);
    }

    private boolean featureWindowIsShowing() {
        return mRestoreOptions.getFeatureQueryResponse() != null;
    }

    private void setRestoreSettings() {
        if(mRestoreSettings != null){
            mRestoreSettings.onSaveInstance(mRestoreOptions, RESTORE_SETTINGS_KEY);
        } else {
            application.setRestoreSettings(new MapFragmentRestoreSettings(mRestoreOptions), RESTORE_SETTINGS_KEY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLayerManager != null) {
            mLayerManager.clearVisibleLayers();
        }
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
        mPanelManager.hide();
        saveMapState();
    }

    @Override
    public void onDestroyView(){
        mPanelManager.hide();
        super.onDestroyView();
        mLayerManager.clearVisibleLayers();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mMapView != null) {
            mMapView.onPause();
        }

        if(mLocationClient != null && mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
            mLocationClient.disconnect();
            mLocationClient = null;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new GetLibraryImportFolderTask(requestCode, resultCode, data).execute();
        }
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
        PolygonOptions options = new PolygonOptions();
        options.strokeColor(invertColor(feature.getStrokeColor()));
        options.fillColor(invertColor(feature.getFillColor()));
        options.strokeWidth(feature.getStrokeWidth());

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        Polygon polygon = mMap.addPolygon(options);

        mHighlightedPolygons.add(polygon);
    }

    public void highlight(Polyline feature){
        PolylineOptions options = new PolylineOptions();
        options.color(invertColor(feature.getColor()));

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        Polyline polyline = mMap.addPolyline(options);

        mHighlightedPolylines.add(polyline);
    }

    public void highlight(Marker feature, boolean zoomTo){
        clearHighlights();

        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(feature.getPosition());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crosshairs_gps_white_24dp));

        mHighlightedMarker = mMap.addMarker(options);

        if(zoomTo) {
            zoomToFeature(mHighlightedMarker);
        }
    }

    protected void zoomToFeature(Marker highlightedMarker) {
        CameraUpdate finalUpdate;

        if(mIsLandscape){
            finalUpdate = CameraUpdateFactory.newLatLng(highlightedMarker.getPosition());
        } else {
            float zoom = mMap.getCameraPosition().zoom;

            CameraUpdate update = CameraUpdateFactory.newLatLng(highlightedMarker.getPosition());

            mMap.moveCamera(update);

            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            LatLngBounds dblBounds = createDoubleBounds(bounds);

            CameraUpdate update2 = CameraUpdateFactory.newLatLngBounds(dblBounds, 0);

            mMap.animateCamera(update2);

            double latDiff = (dblBounds.getCenter().latitude - highlightedMarker.getPosition().latitude) / 2;

            double latBtwCenterMarker = highlightedMarker.getPosition().latitude + latDiff;

            finalUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latBtwCenterMarker, dblBounds.getCenter().longitude), zoom);
        }


        mMap.animateCamera(finalUpdate);
    }

    protected void zoomToCluster(Marker clusterMarker){
        LatLng position = clusterMarker.getPosition();

        LatLng ll = new LatLng(position.latitude, position.longitude);

        float zoom = mMap.getCameraPosition().zoom + 2;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);

        mMap.animateCamera(update);
    }



    public void zoomToHighlights() {
        if(mHighlightedPolygons.size() > 0){
            List<LatLng> points = new ArrayList<>();

            for ( Polygon p : mHighlightedPolygons) {
                points.addAll(p.getPoints());
            }

            polyZoomToFeature(points);

        } else if(mHighlightedPolylines.size() > 0){
            List<LatLng> points = new ArrayList<>();

            for ( Polyline p : mHighlightedPolylines) {
                points.addAll(p.getPoints());
            }

            polyZoomToFeature(points);

        } else {

        }
    }

    public void polyZoomToFeature(List<LatLng> points){
        final LatLngBounds bounds = getBounds(points);

        CameraUpdate update;
        if(mIsLandscape){
            update = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        } else {
            LatLngBounds verticalDoubleBounds = createDoubleBounds(bounds);

            update = CameraUpdateFactory.newLatLngBounds(verticalDoubleBounds, 50);
        }

        mMap.animateCamera(update);

    }

    private LatLngBounds createDoubleBounds(LatLngBounds bounds) {
        double diff = bounds.northeast.latitude - bounds.southwest.latitude;

        double latitude = bounds.southwest.latitude - diff;

        LatLng southwest = new LatLng(latitude, bounds.southwest.longitude);

        return new LatLngBounds(southwest , bounds.northeast);
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

        for(Polyline polyline : mHighlightedPolylines){
            polyline.remove();
        }
        for(Polygon gon : mHighlightedPolygons){
            gon.remove();
        }

        mHighlightedPolygons.clear();
        mHighlightedPolylines.clear();
        mRestoreOptions.setFeatureQueryResponse(null);
    }

    protected Boolean highlightLine(LatLng position){
        Boolean foundMatch = false;

        Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

        double tolerance = calculateTolerance(mMap.getCameraPosition().zoom);

        for (Polyline line : lines) {
            if (PolyUtil.isLocationOnPath(position, line.getPoints(), false, tolerance)) { //idea: reset tolerance by zoom level
                getFeatureWindow(line.getId(), LayerManager.LINE);

                foundMatch = true;
                break;
            }
        }

        return foundMatch;
    }

    public double calculateTolerance(float zoom) {
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

    protected boolean  highlightPolygon(LatLng position){
        Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

        for (Polygon ss : polygons) {
            if (PolyUtil.containsLocation(position, ss.getPoints(), true)) {
                getFeatureWindow(ss.getId(), LayerManager.POLYGON);
                return true;
            }

        }

        return false;
    }
    //endregion

    public void getFeatureWindow(String geomId, int shapeCode){
        mStatusBarManager.setMessage(getString(R.string.loading_feature_window));

        mSelectedFeatureId = mLayerManager.getFeatureId(geomId, shapeCode);
        mSelectedLayerId = mLayerManager.getLayerId(geomId, shapeCode);

        if(shapeCode == LayerManager.POLYGON){
            highlightAssociatedPolygons(shapeCode);
        }

        if(shapeCode == LayerManager.LINE){
            highlightPolylinesAssociated(shapeCode);
        }

        zoomToHighlights();

        mQueryService.featureWindow(mSelectedFeatureId, mSelectedLayerId);
    }

    public void highlightAssociatedPolygons(int shapeCode) {
        clearHighlights();

        List<String> markerIds = mLayerManager.getAssociatedShapes(mSelectedFeatureId, shapeCode);

        Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

        for (Polygon ss : polygons) {
            for(String markerId : markerIds){
                if(markerId.equals(ss.getId())){
                    highlight(ss);
                }
            }
        }
    }

    public void highlightPolylinesAssociated(int shapeCode) {
        clearHighlights();

        List<String> markerIds = mLayerManager.getAssociatedShapes(mSelectedFeatureId, shapeCode);

        Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

        for (Polyline ss : lines) {
            for(String markerId : markerIds){
                if(markerId.equals(ss.getId())){
                    highlight(ss);
                }
            }
        }
    }


    public void refreshFeatureWindow(int tab){
        mStatusBarManager.setMessage(getString(R.string.refreshing_feature_window));

        setFeatureWindowTab(tab);

        mQueryService.featureWindow(mSelectedFeatureId, mSelectedLayerId);

    }

    public void setFeatureWindowTab(int tab){
        mFeatureWindowTabToShow = tab;
    }

    protected void initializeGoogleMap() {

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

                mRestoreOptions.setFeatureQueryResponse(response);
                mRestoreOptions.setLayerId(mSelectedLayerId);

                if(application.getIsLandscape()){
                    landscapeFeatureWindow(response);
                } else {
                    portraitFeatureWindow(response);
                }
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

    private void landscapeFeatureWindow(ParcelableFeatureQueryResponse response) {
        Fragment f = new FeatureWindowPanelFragment().initialize(mSelectedLayerId);

        f.setArguments(response.toBundle());

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_frame, f)
                .commit();

        ((MainActivity)getActivity()).showDetailFragment();

        mStatusBarManager.reset();
    }

    private void portraitFeatureWindow(ParcelableFeatureQueryResponse response) {
        mPanelManager = new PanelManager(GeoPanel.MAP);

        Fragment f = new FeatureWindowPanelFragment().initialize(mSelectedLayerId);

        f.setArguments(response.toBundle());

        application.getMainActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.slider_content, f)
                .commit();

        mPanelManager.halfAnchor();
        mPanelManager.touch(true);
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
        setStandardMapOnClicks();

        mLayerManager.showLayers(mMap);

        mRestoreSettings = (MapFragmentRestoreSettings)application.getRestoreSettings(RESTORE_SETTINGS_KEY);

        if(mRestoreSettings != null){
            SettingsConfig settingsConfig = mRestoreSettings.getEntityConditionally(null);


            if(settingsConfig != null){
                ParcelableFeatureQueryResponse featureWindowData = settingsConfig.getFeatureQueryResponse();
                mSelectedLayerId = settingsConfig.getLayerId();

                showFeatureWindow(featureWindowData);
            }


        }
    }

    public void getNextFeature() {
        //Get Next Feature to the right of the current feature
        //1. get LatLng center of current highlight
        LatLng center = null;
        int typeCode = 0;

        if(mHighlightedMarker != null){
            typeCode = GeometryTypeCodes.Point;

            center = mHighlightedMarker.getPosition();
        }

        if(mHighlightedPolyline != null){
            typeCode = GeometryTypeCodes.Line;

            center = getBounds(mHighlightedPolyline.getPoints()).getCenter();
        }

        if(mHighlightedPolygon != null){
            typeCode = GeometryTypeCodes.Polygon;

            center = getBounds(mHighlightedPolygon.getPoints()).getCenter();
        }

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, true, center);
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
        LatLng center = null;

        if(mHighlightedMarker != null){
            typeCode = GeometryTypeCodes.Point;
            center = mHighlightedMarker.getPosition();
        }

        if(mHighlightedPolyline != null){
            typeCode = GeometryTypeCodes.Line;
            center = getBounds(mHighlightedPolyline.getPoints()).getCenter();
        }

        if(mHighlightedPolygon != null){
            typeCode = GeometryTypeCodes.Polygon;
            center = getBounds(mHighlightedPolygon.getPoints()).getCenter();

        }

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, false, center);
        }
    }

    public void centerMap(LatLng center) {
        CameraUpdate update = CameraUpdateFactory.newLatLng(center);

        mMap.animateCamera(update);
    }

    public void rezoomToHighlight() {

        if(mHighlightedMarker != null){
            zoomToFeature(mHighlightedMarker);
        } else {
            zoomToHighlights();
        }
    }

    public void editLayer(LegendLayer legendLayer) {
        application.setEditingLayerMode(true);

        if(legendLayer.getCheckBox() != null){
            legendLayer.getCheckBox().setChecked(true);
            legendLayer.getLayer().setIsShowing(true);
        } else {
            legendLayer.getLayer().setIsShowing(true);
        }

        int code = legendLayer.getLayer().getGeometryTypeCodeId();

        mMap.setOnMarkerClickListener(null);
        mMap.setOnMapClickListener(null);
        mMap.setOnCameraChangeListener(null);

        mLayerManager.clearVisibleLayers();

        Layers single = new Layers(legendLayer.getLayer());
        List<Layers> layers = new ArrayList<>();
        layers.add(single);

        MapDefaultQueryRequest request = new MapDefaultQueryRequest(layers, Options.MAP_QUERY);

        ILayerStyleTask layerStyleTask = application.getTasksComponent().provideLayerStyleTask();

        layerStyleTask.getStyle(legendLayer, new EditLayerMapQueryRequestCallback(request, legendLayer));

        switchActionBarVisibility(mEditActionBar, mMapActionBar);

        mRedoBtn.setVisibility(View.VISIBLE);
        mUndoBtn.setVisibility(View.VISIBLE);

        mExtentBtn.setVisibility(View.GONE);
        mLocationBtn.setVisibility(View.GONE);

        Fragment f = null;

        if(code == GeometryTypeCodes.Point || code == GeometryTypeCodes.MultiPoint){
            //1. Show Panel Fragment
            f = new EditPointPanelFragment();

            mEditor = new PointLayerEditor(legendLayer, mMap, getActivity());

            ((EditPointPanelFragment)f).init(mEditor);

        } else if(code == GeometryTypeCodes.Line || code == GeometryTypeCodes.MultiLine){
            f = new EditLinePanelFragment();

            mEditor = new LineLayerEditor(legendLayer, mMap, getActivity());

            ((EditLinePanelFragment)f).init(mEditor);

        } else if (code == GeometryTypeCodes.Polygon || code == GeometryTypeCodes.MultiPolygon){
            f = new EditPolygonPanelFragment();

            mEditor = new PolygonLayerEditor(legendLayer, mMap, getActivity());

            ((EditPolygonPanelFragment)f).init(mEditor);

        } else{
            try {
                throw new Exception("Not Sure What the user is trying to Edit");
            } catch (Exception e) {
                mAnalytics.sendException(e);
            } finally {
                return;
            }
        }

        mEditor.setUndoRedoListeners(mUndoBtn, mRedoBtn);

        application.getMainActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.slider_content, f)
                .commit();

        mPanelManager.halfAnchor(-0.40f);
    }

    protected void switchActionBarVisibility(RelativeLayout actionBartoShow, RelativeLayout actionBartoHide) {
        actionBartoShow.setVisibility(View.VISIBLE);
        actionBartoHide.setVisibility(View.GONE);
    }

    public Boolean getIsLandscape() {
        return application.getIsLandscape();
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

    public static class MapFragmentRestoreSettings extends RestoreSettings<SettingsConfig>{
        public MapFragmentRestoreSettings(SettingsConfig config){ super(config); }
    }

    public static class SettingsConfig {
        ParcelableFeatureQueryResponse mFeatureQueryResponse;

        public int getLayerId() {
            return LayerId;
        }

        public void setLayerId(int layerId) {
            LayerId = layerId;
        }

        int LayerId;

        public ParcelableFeatureQueryResponse getFeatureQueryResponse() {
            return mFeatureQueryResponse;
        }

        public void setFeatureQueryResponse(ParcelableFeatureQueryResponse featureQueryResponse) {
            mFeatureQueryResponse = featureQueryResponse;
        }
    }

}
