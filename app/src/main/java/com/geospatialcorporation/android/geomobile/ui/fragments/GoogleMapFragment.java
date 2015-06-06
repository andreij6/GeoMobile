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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.QueryMode;
import com.geospatialcorporation.android.geomobile.library.viewmode.implementations.SearchMode;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IViewModeListener;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.SlidingPanelController;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapTypeSelectDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.CollapsedPanelFragment;
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
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
        SlidingPanelController,
        IViewModeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    //region Properties
    public GoogleMap mMap;
    IViewMode mViewMode;
    GoogleApiClient mLocationClient;
    @InjectView(R.id.map) MapView mView;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @InjectView(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
    @InjectView(R.id.fab_point) FloatingActionButton mPointQueryBtn;
    @InjectView(R.id.fab_close) FloatingActionButton mCloseBtn;
    @InjectView(R.id.fab_save) FloatingActionButton mSaveBtn;
    @InjectView(R.id.fab_bm_close) FloatingActionButton mBmClose;
    Menu mMenu;
    //endregion

    //region OnClicks
    @SuppressWarnings("unused")
    @OnClick(R.id.fab_location)
    public void getLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

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
        DrawerLayout mDrawerLayout = ((MainActivity)getActivity()).getRightDrawer();
        View layerView = ((MainActivity)getActivity()).getLayerListView();

        mDrawerLayout.openDrawer(layerView);
    }

    //endregion

    //region Constructors
    public GoogleMapFragment() {
        // Empty constructor required for fragment subclasses
    }
    //endregion

    //region Base Overrides
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);


        ButterKnife.inject(this, rootView);
        SetTitle(R.string.app_name);

        initializeGoogleMap(savedInstanceState);

        setCollapsedUI();
        setupPanel();

        return rootView;
    }


    protected void initializeGoogleMap(Bundle savedInstanceState) {
        mView.onCreate(savedInstanceState);

        mMap = mView.getMap();

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
        mView.onResume();
        setMapState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                MapTypeSelectDialogFragment m = new MapTypeSelectDialogFragment();
                m.setContext(getActivity());
                m.setMap(mMap);
                m.show(getFragmentManager(), "styles");
                return true;
            case R.id.action_search:
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                setViewMode(searchSetup());
                return true;
            case R.id.action_query:
                setViewMode(querySetup());
                return true;
            case R.id.action_bookmark:
                GeoDialogHelper.showBookmarks(getActivity(), getFragmentManager(), mSaveBtn, mBmClose, mPanel, mMap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mView.onLowMemory();
    }

    @Override
    public void onStop(){
        super.onStop();
        saveMapState();
    }

    private void saveMapState() {
        MapStateManager msm = new MapStateManager(getActivity());
        msm.saveMapState(mMap);
        disableViewMode();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mView.onDestroy();
    }

    private void disableViewMode() {
        if(mViewMode != null){
            mViewMode.Disable(true);
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        disableViewMode();
    }
    //endregion

    private void setupPanel() {

        mPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                setCollapsedUI();
                MenuItem item = mMenu.findItem(R.id.action_search);
                item.setVisible(true);
            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    private void setCollapsedUI() {

        Fragment collapsedFragment = new CollapsedPanelFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.slider_content, collapsedFragment)
                .commit();
    }

    private IViewMode querySetup() {
        return new QueryMode.Builder()
                .setDependents(mMap, mListener, getActivity())
                .setControls(mBoxQueryBtn, mPointQueryBtn, mCloseBtn)
                .setupUI(mPanel)
                .create();

    }

    protected IViewMode searchSetup() {

        return new SearchMode.Builder()
                        .init(mPanel, getActivity().getSupportFragmentManager())
                        .create();
    }

    //region Test Layers
    /*

     //Sets up the map if it is possible to do so

    public void setUpMapIfNeeded() {
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            testMapMarker();
            testMapCircle();
            testMapRaster();
        }
    }


    //This is where we can add markers or lines, add listeners or move the camera. In this case, we
    //just add a marker near Africa.
    //<p/>
    //This should only be called once and when we are sure that {@link #mMap} is not null.

    private void testMapMarker() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(38.3448001, -96.5878515)).title("Marker"));
    }

    private void testMapCircle() {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(38.3448001, -96.5878515))
                .strokeColor(Color.BLUE)
                .fillColor(0x330000FF)
                .radius(30432.02 * 10)); // radius in meters || 1m == 0.000621371mi


    }

    private void testMapRaster(){
        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
        LatLng northeast = new LatLng(40.5118451,-79.6953338);
        LatLng southwest = new LatLng(40.3947741,-79.9599499);

        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        GroundOverlay overlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                                    .image(image)
                                    .positionFromBounds(bounds)
                                    .transparency(0.5f));

    } **/
    //endregion

    //region GoogleAPIClient Interface Methods
    /*@Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(getActivity(), "Connected to location service", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //
    }
    **/
    //endregion

    //region SlidingPanelController Interface
    @Override
    public void CollapsePanel() {
        mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mPanel.setTouchEnabled(true);
    }
    //endregion

    //region ViewMode Listener
    @Override
    public void setViewMode(IViewMode mode) {
        if(mViewMode != null){
            if(!mViewMode.isSame(mode)){
                if(mode instanceof SearchMode){
                    mViewMode.Disable(true);
                    mViewMode = null;
                    mViewMode = mode;
                } else {
                    MenuItem item = mMenu.findItem(R.id.action_search);
                    item.setVisible(false);

                    mViewMode.Disable(false);
                    mViewMode = null;
                    mViewMode = mode;
                }

            }
        } else {
            if(mode instanceof SearchMode){
                mViewMode = mode;
            } else {
                MenuItem item = mMenu.findItem(R.id.action_search);
                item.setVisible(false);
                mViewMode = mode;
            }

        }


    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
    //endregion


}
