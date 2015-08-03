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
import android.graphics.drawable.shapes.Shape;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.LayerManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureWindowPanelFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.PolyUtil;

import java.util.Collection;
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
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    //region Properties
    public GoogleMap mMap;
    IViewMode mViewMode;
    GoogleApiClient mLocationClient;
    @InjectView(R.id.map)
    ILayerManager mLayerManager;
    //    ISlidingPanelManager mPanelManager;
//    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
//    @InjectView(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
//    @InjectView(R.id.fab_point) FloatingActionButton mPointQueryBtn;
//    @InjectView(R.id.fab_close) FloatingActionButton mCloseBtn;
//    @InjectView(R.id.fab_save) FloatingActionButton mSaveBtn;
//    @InjectView(R.id.fab_bm_close) FloatingActionButton mBmClose;
//    @InjectView(R.id.fab_fullscreen_close) FloatingActionButton mFullScreenClose;
    Menu mMenu;
    //endregion

    //region OnClicks
    @SuppressWarnings("unused")
    @OnClick(R.id.fab_location)
    public void getLocation() {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        mAnalytics.trackClick(new GoogleAnalyticEvent().CurrentLocation());

        if (currentLocation == null) {
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
        mAnalytics.trackClick(new GoogleAnalyticEvent().OpenLayerDrawer());
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
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (mMapView != null) {
            mMapView.onCreate(savedInstance);
        }

        application.setMapFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.inject(this, rootView);
        SetTitle(R.string.app_name);

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapScreen());



        initializeGoogleMap(savedInstanceState);

        //region Show Feature Window Code

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getFeatureWindow(marker.getId(), LayerManager.POINT);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

                for (Polyline line : lines) {
                    if (PolyUtil.isLocationOnPath(latLng, line.getPoints(), false, 200.0)) { //idea: reset tolerance by zoom level
                        getFeatureWindow(line.getId(), LayerManager.LINE);

                    }
                }

                Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

                for (Polygon ss : polygons) {
                    if (PolyUtil.containsLocation(latLng, ss.getPoints(), true)) {
                        getFeatureWindow(ss.getId(), LayerManager.POLYGON);
                    }

                }


            }
        });

        //endregion

        return rootView;
    }

    protected void getFeatureWindow(String id, int shapeCode){
        String featureId = mLayerManager.getFeatureId(id, shapeCode);
        int layerId = mLayerManager.getLayerId(id, shapeCode);
        QueryRestService QueryService = new QueryRestService();
        QueryService.featureWindow(featureId, layerId);
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

        mLayerManager.showLayers();

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
//        inflater.inflate(R.menu.map_menu, menu);
//        mMenu = menu;
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setMapState();
        mPanelManager.collapse();
        mLayerManager.showLayers();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action buttons
//        switch (item.getItemId()) {
//            case R.id.action_map_settings:
                mAnalytics.trackClick(new GoogleAnalyticEvent().ChangeBaseMap());
//                m.setContext(getActivity());
//                m.setMap(mMap);
//                m.show(getFragmentManager(), "styles");
//                return true;
//            case R.id.action_fullscreen:
            //case R.id.action_fullscreen:
            //    mPanelManager.hide();
            //    setViewMode(fullScreenSetup());
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onDestroy() {
        mLayerManager.clearVisibleLayers();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveMapState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLayerManager.clearVisibleLayers();
        if (mViewMode != null) {
            mViewMode.Disable(true);
            mViewMode = null;
        }
        //mPanelManager.hide();
       // mPanelManager.collapse();
    }

    @Override
    public void onPause() {
        mLayerManager.clearVisibleLayers();
        mMapView.onPause();
        super.onPause();
    }

    //endregion

    //region ViewModeSetups
//    protected IViewMode querySetup() {
//        mAnalytics.trackClick(new GoogleAnalyticEvent().QueryModeInit());

//                .setControls(mBoxQueryBtn, mPointQueryBtn, mCloseBtn, getActivity().getSupportFragmentManager())
//                .setupUI()
//                .create();
//
//    }

//    protected IViewMode searchSetup() {
//        return new SearchMode.Builder()
//                        .init(getActivity().getSupportFragmentManager(), mPanelManager)
//        mAnalytics.trackClick(new GoogleAnalyticEvent().QuickSearchInit());
//    }

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

        if (mViewMode != null) {
            if (mViewMode.isSame(mode)) {
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
        MapStateManager msm = new MapStateManager(getActivity());
        msm.saveMapState(mMap);
    }

    protected void setMapState() {
        MapStateManager msm = new MapStateManager(getActivity());

        CameraPosition position = msm.getSavedCameraPosition();
        Integer mapType = msm.getSavedMapType();

        mMap.setMapType(mapType);

        if(position != null){
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
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

}
