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
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.helpers.MarkerComparer;
import com.geospatialcorporation.android.geomobile.library.helpers.QueryMachine;
import com.geospatialcorporation.android.geomobile.library.helpers.QuerySenderHelper;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Query.point.Max;
import com.geospatialcorporation.android.geomobile.models.Query.point.Min;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.SlidingPanelController;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapTypeSelectDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.CollapsedPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.QuickSearchFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends GeoViewFragmentBase implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SlidingPanelController
{
    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    //region Properties
    public GoogleMap mMap;

    Polygon mShape;
    List<Marker> markers = new ArrayList<>();
    private static final int BOX = 2;
    QueryMachine mQueryMachine;

    GoogleApiClient mLocationClient;
    @InjectView(R.id.map) MapView mView;
    @InjectView(R.id.fab_location) FloatingActionButton mMyCurrentButton;
    @InjectView(R.id.fab_layers) FloatingActionButton mLayersButton;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @InjectView(R.id.fab_box) FloatingActionButton mBoxQueryBtn;
    @InjectView(R.id.fab_point) FloatingActionButton mPointQueryBtn;
    @InjectView(R.id.fab_close) FloatingActionButton mCloseBtn;
    @InjectView(R.id.slider_content) RelativeLayout mSliderContent;
    private boolean mIsQuerying;
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

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_box)
    public void boxQuery_StepOne(){

        mQueryMachine.setState(QueryMachine.State.BOXQUERY_INPROGRESS);

        clearMapQueryPoints();
        SetTitle(R.string.box_query);

        Toaster("Click on the map in 2 diagonal positions");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng ll) {
                boxQueryStep2AddPoint(ll.latitude, ll.longitude);
            }
        });
        
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_point)
    public void setupPointQuery(){
        mQueryMachine.setState(QueryMachine.State.POINTQUERY_INPROGRESS);

        clearMapQueryPoints();
        SetTitle(R.string.point_query);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setPointMarker(latLng.latitude, latLng.longitude);
            }
        });
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.fab_close)
    public void reset(){
        if(mQueryMachine.isState(QueryMachine.State.POINTQUERY_DRAWN)
                || mQueryMachine.isState(QueryMachine.State.BOXQUERY_DRAWN)
                || mQueryMachine.isState(QueryMachine.State.BOXQUERY_INPROGRESS)){

            clearMapQueryPoints();

            switch (mQueryMachine.getState()){
                case QueryMachine.State.POINTQUERY_DRAWN:
                    mQueryMachine.setState(QueryMachine.State.POINTQUERY_INPROGRESS);
                    break;
                case QueryMachine.State.BOXQUERY_DRAWN:
                case QueryMachine.State.BOXQUERY_INPROGRESS:
                    mQueryMachine.setState(QueryMachine.State.BOXQUERY_STARTED);
                    break;
            }
        } else {

            if (mQueryMachine.isState(QueryMachine.State.PICKQUERY)
                    || mQueryMachine.isState(QueryMachine.State.POINTQUERY_INPROGRESS)
                    || mQueryMachine.isState(QueryMachine.State.BOXQUERY_STARTED)) {

                resetHelper();
            }
        }

    }

    private void resetHelper() {
        toggleCollapsedHidden();
        clearMapQueryPoints();
        mMap.setOnMapClickListener(null);
        SetTitle(R.string.app_name);
    }

    //endregion

    //region Constructors
    public GoogleMapFragment() {
        // Empty constructor required for fragment subclasses
    }
    //endregion

    //region Drawing Query Helpers
    private void setPointMarker(double latitude, double longitude) {
        mQueryMachine.setState(QueryMachine.State.POINTQUERY_DRAWN);

        MarkerOptions options = getMarkerOptions(latitude, longitude);

        clearMapQueryPoints();
        markers.add(mMap.addMarker(options));

        CameraUpdate u = CameraUpdateFactory.newLatLng(options.getPosition());
        mMap.animateCamera(u);

        pointQueryStep2SendToGeoU();
    }

    private void pointQueryStep2SendToGeoU() {
        Toaster("Send to geounderground");
        Toaster("Show results in panel");
    }

    private MarkerOptions getMarkerOptions(double latitude, double longitude) {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_radius_black_48dp))
                .anchor(.5f, .5f);
    }

    protected void clearMapQueryPoints(){
        for(Marker marker : markers){
            marker.remove();
        }
        markers.clear();

        if(mShape != null) {
            mShape.remove();

            mShape = null;
        }
    }

    protected void boxQueryStep2AddPoint(double lat, double lng){
        if(mQueryMachine.isState(QueryMachine.State.BOXQUERY_DRAWN)){
            clearMapQueryPoints();
        }
        MarkerOptions options = getMarkerOptions(lat, lng);

        markers.add(mMap.addMarker(options));

        if(markers.size() == BOX){
            boxQueryStep3CompleteBox();
            mQueryMachine.setState(QueryMachine.State.BOXQUERY_DRAWN);
            boxQueryStep4SendToGeoU();
        } else {
            mQueryMachine.setState(QueryMachine.State.BOXQUERY_INPROGRESS);
        }
    }

    private void boxQueryStep3CompleteBox() {
        LatLng first = markers.get(0).getPosition();
        LatLng second = markers.get(1).getPosition();

        MarkerOptions options = getMarkerOptions(first.latitude, second.longitude);
        MarkerOptions options2 = getMarkerOptions(second.latitude, first.longitude);

        markers.add(mMap.addMarker(options));
        markers.add(mMap.addMarker(options2));

        List<Marker> reOrder = new ArrayList<>();
        reOrder.addAll(markers);

        markers.clear();
        markers.add(reOrder.get(0));
        markers.add(reOrder.get(2));
        markers.add(reOrder.get(1));
        markers.add(reOrder.get(3));

        setCameraToBoxExtent();
    }

    private void setCameraToBoxExtent() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker m : markers) {
            builder.include(m.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 80;
        CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(u);
    }

    protected void boxQueryStep4SendToGeoU(){
        PolygonOptions options = new PolygonOptions().fillColor(0x33000000).strokeWidth(2).strokeColor(Color.BLACK);

        for(int i = 0; i < BOX + 2; i++){
            options.add(markers.get(i).getPosition());
        }

        mShape = mMap.addPolygon(options);

        Collections.sort(markers, new MarkerComparer());
        Min min = new Min(markers.get(2));
        Max max = new Max(markers.get(0));

        Log.d(TAG, max.toString());
        Log.d(TAG, min.toString());

        QuerySenderHelper q = new QuerySenderHelper();
        q.sendBoxQuery(max, min);

        Toaster("send data to Geounderground for Analysis");
        //clearMapQueryPoints();

        //anchorSlider();

    }

    //endregion

    //region Base Overrides
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.inject(this, rootView);

        mView.onCreate(savedInstanceState);

        mMap = mView.getMap();

        setCollapsedUI();

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

        SetTitle(R.string.app_name);

        setupPanel();

        setUpMapIfNeeded();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
        MapStateManager msm = new MapStateManager(getActivity());

        CameraPosition position = msm.getSavedCameraPosition();
        Integer mapType = msm.getSavedMapType();

        mMap.setMapType(mapType);

        if(position != null){
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                MapTypeSelectDialogFragment m = new MapTypeSelectDialogFragment();
                m.setContext(getActivity());
                m.setMap(mView);
                m.show(getFragmentManager(), "styles");
                return true;
            case R.id.action_search:
                searchSetup();
                return true;
            case R.id.action_query:
                querySetup();
                return true;
            case R.id.action_bookmark:
                Toaster("Allow the user to bookmark a position - or go to bookmarked positions");
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
        MapStateManager msm = new MapStateManager(getActivity());
        msm.saveMapState(mMap);
        disableQueryMode();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        disableQueryMode();
    }
    //endregion

    private void setupPanel() {

        mPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                disableQueryMode();
            }

            @Override
            public void onPanelCollapsed(View view) {
                setCollapsedUI();
                disableQueryMode();

            }

            @Override
            public void onPanelExpanded(View view) {

                disableQueryMode();

            }

            @Override
            public void onPanelAnchored(View view) {
                disableQueryMode();

            }

            @Override
            public void onPanelHidden(View view) {
                enableQueryMode();

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

    private void enableQueryMode() {
        mIsQuerying = true;
        toggleQueryBtns();
    }

    private void disableQueryMode() {
        mIsQuerying = false;
        mMap.setOnMapClickListener(null);
        clearMapQueryPoints();
        toggleQueryBtns();
        SetTitle(R.string.app_name);
    }

    private void querySetup() {
        mQueryMachine = new QueryMachine();
        toggleQueryBtns();
        toggleCollapsedHidden();
    }

    protected void searchSetup() {

        setupSlideUIQuickSearch();

        anchorSlider();
    }

    private void setupSlideUIQuickSearch() {
        mPanel.setTouchEnabled(false);

        Fragment quickSearchFragment = new QuickSearchFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.slider_content, quickSearchFragment)
                .commit();
    }

    private void toggleQueryBtns() {
        if(mIsQuerying) {
            mBoxQueryBtn.setVisibility(View.VISIBLE);
            mPointQueryBtn.setVisibility(View.VISIBLE);
            mCloseBtn.setVisibility(View.VISIBLE);
        } else {
            mBoxQueryBtn.setVisibility(View.GONE);
            mPointQueryBtn.setVisibility(View.GONE);
            mCloseBtn.setVisibility(View.GONE);
        }
    }

    private void anchorSlider() {
        if (mPanel != null) {
            if (mPanel.getAnchorPoint() == 1.0f || mPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mPanel.setAnchorPoint(0.7f);
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            } else {
                mPanel.setAnchorPoint(1.0f);
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

    protected void toggleCollapsedHidden(){
        if (mPanel != null) {
            if (mPanel.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            } else {
                mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

    //region Test Layers
    /**
     * Sets up the map if it is possible to do so
     */
    public void setUpMapIfNeeded() {
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            testMapMarker();
            testMapCircle();
            testMapRaster();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
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

    }
    //endregion

    //region GoogleAPIClient Interface Methods
    @Override
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
    //endregion

    //region SlidingPanelController Interface
    @Override
    public void CollapsePanel() {
        mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mPanel.setTouchEnabled(true);
    }
    //endregion


}
