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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment that appears in the "content_frame", shows a google-play map
 */
public class GoogleMapFragment extends Fragment {
    public MapView mView;
    public GoogleMap mMap;

    public GoogleMapFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mView = (MapView) rootView.findViewById(R.id.map);
        mView.onCreate(savedInstanceState);

        mMap = mView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpMapIfNeeded();

        return rootView;
    }

    @Override
    public void onResume() {
        mView.onResume();
        super.onResume();
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void testMapCircle() {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(0, 0))
                .radius(30432.02)); // radius in meters || 1m == 0.000621371mi
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

    //private testPolygon

}
